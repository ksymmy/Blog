package com.ksymmy.lucene;

import com.ksymmy.entity.Blog;
import com.ksymmy.util.DateUtil;
import com.ksymmy.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 博客索引类
 *
 * @author ksymmy
 */
public class BlogIndex {

    private Directory dir = null;

    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene"));
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 标准分词器，会自动去掉空格啊，is a the等单词
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);// 将标准分词器配到写索引的配置中
        IndexWriter writer = new IndexWriter(dir, iwc); // 实例化写索引对象
        return writer;
    }

    // 测试读取文档
    public void testIndexReader() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene"));
        IndexReader reader = DirectoryReader.open(dir);
        System.out.println("最大文档数：" + reader.maxDoc());
        System.out.println("实际文档数：" + reader.numDocs());
        reader.close();
    }

    /**
     * 添加博客索引
     *
     * @param blog
     */
    public void addIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();// 获取写索引的实例
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        writer.addDocument(doc);// 添加文档
        // writer.commit();
        writer.close();// close了才真正写到文档中
    }

    /**
     * 更新博客索引
     *
     * @param blog
     * @throws Exception
     */
    public void updateIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        // 将原来id为1对应的文档，用新建的文档替换
        writer.updateDocument(new Term("id", String.valueOf(blog.getId())), doc);
        // writer.commit();
        writer.close();
        System.out.println(doc.getField("content"));
    }

    /**
     * 删除指定博客的索引
     *
     * @param blogId
     * @throws Exception
     */
    public void deleteIndex(String blogId) throws Exception {
        IndexWriter writer = getWriter();
        System.out.println("删除前有" + writer.numDocs() + "个文档");
        writer.deleteDocuments(new Term("id", blogId));// 删除id=1对应的文档
        writer.forceMergeDeletes(); // 强制合并（强制删除），没有索引了
        writer.commit(); // 提交删除，真的删除了
        System.out.println("删除后最大文档数：" + writer.maxDoc());
        System.out.println("删除后实际文档数：" + writer.numDocs());
        writer.close();
    }

    /**
     * 查询博客信息
     *
     * @param q 查询关键字
     * @return List<Blog>
     * @throws Exception
     */
    public List<Blog> searchBlog(String q) throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene"));// SimpleFSDirectory
        IndexWriter indexWriter = getWriter();
        indexWriter.commit();
        //IndexReader reader = DirectoryReader.open(indexWriter, true);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse(q);
        QueryParser parser2 = new QueryParser("content", analyzer);
        Query query2 = parser2.parse(q);
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        TopDocs hits = is.search(booleanQuery.build(), 100);
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Blog> blogList = new LinkedList<Blog>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Blog blog = new Blog();
            blog.setId(Integer.parseInt(doc.get(("id"))));
            blog.setReleaseDateStr(doc.get(("releaseDate")));
            String title = doc.get("title");
            String content = StringEscapeUtils.escapeHtml(doc.get("content"));
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isEmpty(hTitle)) {
                    blog.setTitle(title);
                } else {
                    blog.setTitle(hTitle);
                }
            }
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 200) {
                        blog.setContent(content);
                    } else {
                        blog.setContent(content.substring(0, 200));
                    }
                } else {
                    blog.setContent(hContent);
                }
            }
            blogList.add(blog);
        }
        reader.close();
        //dir.close();
        /*if (IndexWriter.isLocked(dir)) {
            dir.obtainLock(IndexWriter.WRITE_LOCK_NAME).close();
			}*/
        indexWriter.close();
        return blogList;
    }
}
