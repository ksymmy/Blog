<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>�޸ĸ�����Ϣҳ��</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<!--�����ֶ��������ԣ�������ie����ʱ��Ϊ��������ʧ�ܵ��±༭������ʧ��-->
<!--������ص������ļ��Ḳ������������Ŀ����ӵ��������ͣ���������������Ŀ�����õ���Ӣ�ģ�������ص����ģ�������������-->
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">

	function submitData(){
		var nickName=$("#nickName").val();
		var sign=$("#sign").val();
		var proFile=UE.getEditor('proFile').getContent();

		if(nickName==null || nickName==''){
			alert("�������ǳƣ�");
		}else if(sign==null || sign==''){
			alert("���������ǩ����");
		}else if(proFile==null || proFile==''){
			alert("��������Լ�飡");
		}else{
			$("#pF").val(proFile);
			$('#form1').submit();
		}
	}



</script>
</head>
<body style="margin: 10px">
<div id="p" class="easyui-panel" title="�޸ĸ�����Ϣ" style="padding: 10px">
	<form id="form1" action="${pageContext.request.contextPath}/admin/blogger/save.do" method="post" enctype="multipart/form-data">
	 	<table cellspacing="20px">
	   		<tr>
	   			<td width="80px">�û�����</td>
	   			<td>
	   				<input type="hidden" id="id" name="id" value="${currentUser.id }"/>
	   				<input type="text" id="userName" name="userName" style="width: 200px;" value="${currentUser.userName }" readonly="readonly"/>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td>�ǳƣ�</td>
	   			<td><input type="text" id="nickName" name="nickName"  style="width: 200px;"/></td>
	   		</tr>
	   		<tr>
	   			<td>����ǩ����</td>
	   			<td><input type="text" id="sign" name="sign" value="${currentUser.sign }" style="width: 400px;"/></td>
	   		</tr>
	   		<tr>
	   			<td>����ͷ��</td>
	   			<td><input type="file" id="imageFile" name="imageFile" style="width: 400px;"/></td>
	   		</tr>
	   		<tr>
	   			<td valign="top">���˼�飺</td>
	   			<td>
					<script id="proFile" type="text/plain" style="width:100%;height:500px;">����д����</script>
                    <%--<div id="proFile" style="width:100%;height:500px;"></div>--%>
					   <input type="hidden" id="pF" name="proFile"/>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td></td>
	   			<td>
	   				<a href="javascript:submitData()" class="easyui-linkbutton" data-options="iconCls:'icon-submit'">�ύ</a>
	   			</td>
	   		</tr>
	   	</table>
   	</form>
 </div>

 <script type="text/javascript">

    //ʵ�����༭��
    //����ʹ�ù�������getEditor���������ñ༭��ʵ���������ĳ���հ������øñ༭����ֱ�ӵ���UE.getEditor('editor')�����õ���ص�ʵ��
    var ue = UE.getEditor('proFile');

    ue.addListener("ready",function(){
        //ͨ��ajax��������
       UE.ajax.request("${pageContext.request.contextPath}/admin/blogger/find.do",
            {
                method:"post",
                async : false,
                data:{},
                onsuccess:function(result){
                	result = eval("(" + result.responseText + ")");
                	$("#nickName").val(result.nickName);
                	$("#sign").val(result.sign);
                	$("#nickName").val(result.nickName);
       				UE.getEditor('proFile').setContent(result.proFile);
                }
            }
        );
    });

</script>
</body>
</html>