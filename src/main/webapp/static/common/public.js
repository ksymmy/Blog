/**
 * Created by gpu31 on 2016/5/3.
 */

/**
 * --------------------------------------------------公用组件方法----------------------------------------------------------------
 */

//--------------表单点击事件
$(function () {
    $(document).on('click', '.pops_list font', function () {
        if ($(this).next().is(":hidden")) {
            $(this).next().slideDown(500);    //如果元素为隐藏,则将它显现
            $(this).css("background", "url('${pageContext.request.contextPath}/static/images/g2.png') no-repeat left center");
        } else {
            $(this).next().slideUp(500);     //如果元素为显现,则将其隐藏
            $(this).css("background", "url('${pageContext.request.contextPath}/static/images/g1.png') no-repeat left center");
        }
    }).find('.pops_list font');
});

//#########################################直接前台获取参数的方法开始####################################################
/**
 * 获取当前时间
 * @param parameter 参数可以为'1' or '2'
 * @returns {string}
 */
function gySearchNewDate(parameter){
    var currentYear = new Date().getYear() + 1900;
    var maxMonth = new Date().getMonth() + 1;
    if(maxMonth<10){
        maxMonth = "0"+maxMonth;
    }
    var currentDay= new Date().getDate();
    if(currentDay<10){
        currentDay = "0"+currentDay
    }
    var sj='';
    //暂时至返回两种时间格式,后面可以修改
    if(parameter=='1'){//如果参数为'1',则返回当前的年月分
        sj = currentYear+ "-" + maxMonth +"-"+currentDay;
    }else if(parameter=='2'){//如果参数为'2',则返回当前的年度
        sj = currentYear;
    }else if(parameter=='3'){//如果参数为'3',则放回当前的年度与月度组合成的账期
        sj = currentYear+""+maxMonth;
    }
    return sj;
}

/**
 * 根据两个日期，判断相差天数
 * @param sDate1 开始日期 如：2016-11-01
 * @param sDate2 结束日期 如：2016-11-02
 * @returns {number} 返回相差天数
 */
function gyDaysBetween(sDate1,sDate2){
//Date.parse() 解析一个日期时间字符串，并返回1970/1/1 午夜距离该日期时间的毫秒数
    var time1 = Date.parse(new Date(sDate1));
    var time2 = Date.parse(new Date(sDate2));
    var nDays = Math.abs(parseInt((time2 - time1)/1000/3600/24));
    return nDays;
};


/**
 * 前台js获取UUId 的方法
 * @returns {string}
 */
function gyGetUuid(){
    var s = [];
    var hexDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (var i = 0; i < 32; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    //s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

//#########################################直接前台获取参数的方法结束####################################################


//#########################################表单和datagrid和ajax提交的一些操作开始#################################################

/**
 * from表单提交方法
 * @param id 该from表单的ID
 * @param url 提交表单的Url
 * @param fun 提交后需要执行的方法
 * @parm nid 保存方法后面需要关闭的window窗口
 */
function gyFromSubmit(id,url,fun,nid){
    $("#"+id).prop('method','post').form('submit',{
        url:url,
        onSubmit: function(){
            //$.messager.progress();
            var isValid = $(this).form('validate');
            if (!isValid){
                //$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
            }
            return isValid;	// 返回false终止表单提交
        },
        success:function(result){
            result = JSON.parse(result);
            if(result.success){
                //$.messager.progress('close');	// 如果提交成功则隐藏进度条
                TipUtils.bottomRight('','操作成功','info');
                if(nid){
                   $("#"+nid).dialog("close");
                }
                fun(result);
            }else{
                AlertInfo('操作失败');
            }
        }
    })
}


//信息提示框
function AlertInfo(message, fun) {
    if (fun) {
        $.messager.alert("友情提示", message, "info", fun);
    } else {
        $.messager.alert("友情提示", message, "info");
    }

}
//警告提示框
function AlertWarning(message) {
    $.messager.alert("友情提示", message, "warning");
}
//错误提示框
function AlertFormError(message) {
    $.messager.alert("友情提示", message, "error");
}
//错误提示框
function AlertError(xmlHttpRequest) {
    if (xmlHttpRequest.status == 500) {
        $.messager.alert("友情提示", xmlHttpRequest.responseText.split("<title>")[1].split("</title>")[0], "error");
    } else if (xmlHttpRequest.status == 404) {
        $.messager.alert("友情提示", "请求路径找不到，请重试或联系管理员！", "error");
    } else if (xmlHttpRequest.status == 0) {
        $.messager.alert("友情提示", "服务器已停止，请重试或联系管理员！", "error");
    } else {
        $.messager.alert("友情提示", "请求异常，请重试或联系管理员！", "error");
    }
}

function gyFormSubmitByJson(id,url,fun){
    $("#"+id).form('submit',{
        url:url,
        success:function(data){
            data = JSON.parse(data);
            fun(data);
        }
    })
}



/**
 * 删除数据的方法
 * @param url  删除数据的方法url
 * @param fun 删除数据后需要执行的方法
 */
function gyDeleteDataGrid(url,fun){
    $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.ajax({
                url:url,
                method:'post',
                success:function(data){
                    if(data==1){
                        TipUtils.bottomRight('','删除成功','info');
                        fun();
                    }else{
                        AlertInfo('删除失败')
                    }
                }
            })
        }
    });
}

//#########################################表单和datagrid和ajax提交的一些操作结束#################################################



//#########################################windows窗口和dialog窗口的一些方法开始#########################################

/**
 *
 * @param id 需要关闭window窗口的的ID
 */
function gyCloseWindow(id){
    $("#"+id).window("close");
}

/**
 * 在界面定义一个div容器,以便后面再容器中添加window窗口
 * @param divId 界面div容器的ID
 * @param url window远程加载页面的地址
 * @param width window窗口的宽度
 * @param height window窗口的高度
 * @param title window窗口的标题
 * @param onload window窗口在加载远程数据时触发的事件
 * @param closeId window窗口关闭按钮的id,需要绑定click事件
 * @param submitMethod 保存的方法
 */
function gyDivAddDialog(divId,url,width,height,title,onload,closeId,submitMethod){
    var h = $(window).height();
    if(height > h){
        height =  h-50;
    }
    var uuid = gyGetUuid();
    if(url.indexOf("?")>0){
        url += "&t="+Math.random();
    }else{
        url += "?t="+Math.random();
    }
    var content = "<div id='"+uuid+"' data-options='closed:true,collapsible:false,minimizable:false,closable:true' class='easyui-dialog'></div>";
    //在div容器中添加dialog窗口,这样就不用在界面添加很多的dialog窗口容器了
    $("#"+divId).html(content);
    $("#"+uuid).dialog({
        href:url,
        title:title,
        width: width,
        height: height,
        draggable: true,
        resizable: false,
        top:($(window).height()-height)*0.5,
        left:($(window).width()-width)*0.5,
        modal: true,
        onLoad:function(){
            if(onload){
                onload(uuid);
            }
            if(closeId!=null){
                $("#"+closeId).click(function(){
                    $("#"+uuid).dialog("close");
                    if(document.getElementById(uuid)){
                        $("#"+uuid).remove();
                    }
                })
            }

        },buttons:[{
            text:'保存',
            handler:function(){
               if(submitMethod){
                   submitMethod(uuid);
               }
            }
        },{
            text:'关闭',
            handler:function(){
                $("#"+uuid).dialog("close");
                $("#"+uuid).remove();
            }
        }],
        onClose:function(){
            if(document.getElementById(uuid)){
                $("#"+uuid).remove();
            }
        }
    }).dialog("open");
}
/**
 * 通用dialog的弹出公用方法
 * @param id 需要弹出dialog窗口的容器id
 * @param width 定义弹出窗口的宽度
 * @param height 定义弹出窗口的高度
 * @param title 定义弹出窗口的标题
 * @param onSubmit 在form表单提交前需要执行的方法,这个是带参数的方法
 * @param fun 在form表单提交成功后需要执行的方法
 * @param url 远程调用界面的路径(url)
 *
 * 调用实例:
 *1.在jsp页面需要定义一个带id的dialog容器
 * <div id="zdyid" style="display: none; padding: 10px" ></div>
 *2.在相应的js方法里面调用此方法
 * gyDialogOpen('zdyid','/jcdj/xxxxx/mmm',600,300,'自定义标题',function(param){},function(){})
 */
function gyDialogOpen(id,url,width,height,title,onSubmit,fun){
    $("#"+id).show();
    $("#"+id).dialog({
        width: width,
        height: height,
        title: title,
        href:url,
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function () {
                $('#jdcForm').form('submit', {
                    url: sq.jmjcxx.jdc.formAction,
                    onSubmit: function (param) {
                        var isValid = $(this).form('validate');
                        return isValid;	// 返回false终止表单提交
                        onSubmit(param);
                    },
                    success: function (data) {
                        if (data == 'true') {
                            TipUtils.topCenter();
                            $("#"+id).dialog('close');
                            fun();
                        } else {
                            TipUtils.topCenterError();
                        }
                    }
                });
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#"+id).dialog('close');
            }
        }]
    });
}

/***
 * 弹出一个获取treegrid的window窗口,以供选择数据
 * @param inputId   input框的id ---必须要有
 * @param hidId     隐藏框的id  --必须要有
 * @param DivId     主页面需要放置的一个div容器 --必须要有
 * @param tabName 需要查询数据的表名 --必须要有
 * @param FID 需要查询表代表的父节点id --必须要有
 * @param ID  树 的id值 --必须要有
 * @param TEXT 树节点显示值 --必须要有
 * @param TJ  查询条件(不需要则给null) --必须要有
 * @param PidVal 最高父节点id给一个 --必须要有
 * @param fun 远程请求页面的tab了选择一条数据后需要触发的方法
 *
 * 调用实例:
 * 1.jsp页面端:加一个div窗口,以便动态添加window窗口,还有两个input框,一个掩藏,一个显示的,显示的是展现TEXT给用户看,掩藏的是最后需要保存的ID
 *   <input id="select_cs1"  style="width: 180px;"/>
 *   <input  id="ce" name="ce" class="easyui-textbox"  style="width: 180px;"/>
 *   <div id="selectT"></div>
 * 2.js调用方面
 *   gyTcTreegrid("select_cs1","ce","selectT","M_DJ_DZZGL","SJDZZID","DZZID","DZBMC",null,'-',function(row){});
 *
 * */
function gyTcTreegrid(inputId,hidId,DivId,tabName,FID,ID,TEXT,TJ,PidVal,fun,title){
    if(!title){
        title = "选择"
    }
    //根据传过来的inputId来定义该input,增加一个点击图片和点击图片的方法
    $("#"+inputId).textbox({
        icons: [{
            iconCls:'icon-search',
            handler: function(e){
                //为保证在页面添加的window的id保持唯一,所以用input和div的id组合成一个id,以便方法实现,
                var WinId = inputId+DivId;
                var content = "<div id='"+WinId+"' data-options='closed:true,collapsible:false,minimizable:false' class='easyui-window'></div>";
                //在div容器中添加window窗口,这样就不用在界面添加很多的window窗口容器了
                $("#"+DivId).html(content);
                gyWindowOpen(WinId,"/jcdj/Common/selectText",function(){
                    //判断传过来的方法存不存在
                    loadTrees(tabName,FID,ID,TEXT,TJ,PidVal,fun);
                    //这边是给远程页面的一个按钮绑定一个点击事件
                    $("#btnQr").click(function(){
                        //获得远程页面table(不确定时候选中)选中的数据
                        var selectRow = $("#tabTree").treegrid('getSelected');
                        //判断是否选中数据,若选中则给传过来的inputId和hidId赋值,若没有,则不会赋值
                        if(selectRow){
                            $(e.data.target).textbox('setValue',selectRow.TEXT);
                            $("#"+hidId).val(selectRow.ID);
                        }
                        //上述方法处理完之后,需要关闭窗口
                        gyCloseWindow(WinId);
                        $("#"+WinId).remove();
                    });

                },630,400,title)
            }
        }]
    });
    //再给输入款添加一个双击事件,以防需要
    $("input",$("#"+inputId).next("span")).dblclick(function(){
        //为保证在页面添加的window的id保持唯一,所以用input和div的id组合成一个id,以便方法实现,
        var WinId = inputId+DivId;
        var content = "<div id='"+WinId+"' data-options='closed:true,collapsible:false,minimizable:false' class='easyui-window'></div>";
        //在div容器中添加window窗口,这样就不用在界面添加很多的window窗口容器了
        $("#"+DivId).html(content);
        gyWindowOpen(WinId,"/jcdj/Xqgl/selectText",function(){
            //判断传过来的方法存不存在
            loadTrees(tabName,FID,ID,TEXT,TJ,PidVal,fun);
            //这边是给远程页面的一个按钮绑定一个点击事件
            $("#btnQr").click(function(){
                //获得远程页面table(不确定时候选中)选中的数据
                var selectRow = $("#tabTree").treegrid('getSelected');
                //判断是否选中数据,若选中则给传过来的inputId和hidId赋值,若没有,则不会赋值
                if(selectRow){
                    $("#"+inputId).textbox('setValue',selectRow.TEXT);
                    $("#"+hidId).val(selectRow.ID);
                }
                //上述方法处理完之后,需要关闭窗口
                gyCloseWindow(WinId);
                $("#"+WinId).remove();
            });

        },630,400,title)
    })
}

//#########################################windows窗口和dialog窗口的一些方法结束#########################################


//#########################################一些input调用控件的方法开始###################################################
/**
 * 加载combogrid的方法
 * @param domId 输入框的id值
 * @param id idField的值
 * @param text textField
 * @param panelWidth panelWidth的值
 * @param url 访问数据的方法url
 * @param columns 列
 * @param fun 需要用到的方法
 * @param readonly 是否只读
 * @constructor
 */
function gyShowcombogrid(domId,id,text,panelWidth,url, columns,fun,onload,readonly) {
    $("#" + domId).combogrid({
        url: url,
        delay: 800,
        idField: id,
        panelWidth: panelWidth,
        textField: text,
        loadMsg: '正在加载...',
        pagination: true,
        mode: 'remote',
        pageList: [10, 20],
        columns:columns,
        fitColumns:true,
        readonly:readonly?readonly:false,
        onSelect: function (index, row) {
            fun(row);
        },
        onLoadSuccess: function (data) {
            if(onload){
                onload(data);
            }
        }
    });
    var pager = $('#' + domId).combogrid("grid").datagrid('getPager');
    pager.pagination({
        showPageList: false,
        showRefresh: false,
        displayMsg: ''
    });
}

/**
 * 加载combobox方法
 * @param id 选择框的id
 * @param url 加载数据的地址
 * @param valueField combobox value的设定
 * @param textField combobox text的设定
 * @param fun 需要执行的onChange方法
 */
function gyShowCombobox(id,url,valueField,textField,fun){
    $("#"+id).combobox({
        url:url,
        valueField:valueField,
        textField:textField,
        onChange:function(newValue,oldValue){
            if(fun){
                fun(newValue)
            }
        }
    })
}

/**
 * 简单的公用combotree的调用方法
 * @param id   input框的id值
 * @param tabName 需要查询数据的表名
 * @param PID 需要查询表代表的父节点id
 * @param ID  树 的id值
 * @param TEXT 树节点显示值
 * @param TJ  查询条件(不需要则给null)
 * @param PidVal 最高父节点id值一个
 * @param value 初始化完成后需要赋值的值
 * @param fun 选择一个节点后需要触发的事件
 */
function gyComboTree(id,tabName,PID,ID,TEXT,TJ,PidVal,value,onSelectNode,isLeaf,width,readonly) {
    var url = "/jcdj/Common/queryCombotree";
    var queryParams = {tabName:tabName,PID:PID,ID:ID,TEXT:TEXT,PidVal:PidVal,key:id};
    if(TJ){
        queryParams.TJ = encodeURI(TJ);
    }
    if(!width){
        width = 200;
    }
    if(readonly===undefined) readonly = false;
    $('#' + id).combotree({
        url: url,
        queryParams: queryParams,
        panelWidth: 350,
        panelHeight: 200,
        width: width,
        delay: 200,
        lines: true,
        editable: true,
        readonly:readonly,
        onLoadSuccess: function () {
            var tree = $('#' + id).combotree('tree');
            var node = tree.tree("getRoot");
            if (node) {
                $('#' + id).combotree("setValue", node.id);
            }
            $("#" + id).next().children().eq(1).bind("input propertychange", function () {
                $('#' + id).combotree("showPanel");
                //得到输入的内容
                var inputText = $('#' + id).combotree("getText");
                //得到树
                var tree = $('#' + id).combotree("tree");
                if (inputText == '') {
                    var parent = tree.tree("getRoots")[0];
                    tree.tree("expandTo", parent.target);
                    tree.tree("scrollTo", parent.target);
                    tree.tree("select", parent.target);
                    return;
                }
                //得到所有的treenode
                if (/^[a-zA-Z]*$/.test(inputText)) {
                    inputText = inputText.toLowerCase();
                    var treeNodes = $(".tree-node");
                    if (treeNodes) {
                        $.each(treeNodes, function (i, s) {
                            // s:dom
                            var thisNode = tree.tree("getNode", $(s));
                            if (thisNode.charHead.indexOf(inputText) != -1) {
                                tree.tree("expandTo", thisNode.target);
                                tree.tree("scrollTo", thisNode.target);
                                tree.tree("select", thisNode.target);
                                return;
                            }
                        })
                    }
                }
                //得到这个节点集合
                var nodes = $(".tree-title:contains('" + inputText + "')");
                if (nodes) {
                    $.each(nodes, function (i, s) {
                        var node = $(s).parent();
                        tree.tree("expandTo", node);
                        tree.tree("scrollTo", node);
                        tree.tree("select", node);
                        return;
                    });
                }
            })
            if (value!=null) {
                var n = tree.tree('find', value);
                tree.tree("expandTo", n.target);
                tree.tree("scrollTo", n.target);
                $('#' + id).combotree("setValue", value);
            }
        },
        onBeforeSelect: function (node) {
            if (node != null) {
                if(!isLeaf){
                    if (!$(this).tree("isLeaf", node.target)) {
                        return false;
                    }
                }

            }
        },
        onClick: function (node) {
            if(isLeaf){
                if (!$(this).tree("isLeaf", node.target)) {
                    $("#" + id).combo("hidePanel");
                }
            }
        },
        onSelect: function (node) {
            if (node != null) {
                $("#" + id).combotree("hidePanel");
                if (onSelectNode) {
                    onSelectNode(node);
                }
            }
            return true;
        }
    });
}

//#########################################一些input调用控件的方法结束##################################################

//##########################################多选值拼成字符串开始########################################################
/**
 * 根据传过来的combogrid、datagrid 的list集合获取相应字段组成的String类型的值
 * @param list 传过来的集合
 * @param ids 传过来需要获取combogrid、datagrid那个字段
 * @returns 返回相应字段值组成的String类型的值
 * 使用举例(获取该datagrid中所有被选中的id字段的值)
 * var selectRows = $("#tab").datagrid("getSelections");
 * var ids = gydataGridValues(selectRows,"id");
 */
var gydataGridValues = function(list,ids){
    var nid="";
    ids = "list[i]."+ids;
    for(var i= 0;i<list.length;i++){
        if(i==list.length-1){
            nid +="'"+eval("("+ids+")")+"'";
        }else{
            nid +="'"+eval("("+ids+")")+"',";
        }
    }
    return nid;
};

//##########################################多选值拼成字符串结束#######################################################


function GydownloadUrl(url,path,filename){
    var filePath = url+"/fileManage/down.action?path=" + encodeURI(encodeURI(path));
    var htmlArr = ['<a href="', filePath, '"', '>', filename, '</a>'];
    return htmlArr.join("");
}


/**
 * 公用上传的方法
 * @param domId div容器的id
 * @param callback 回调函数
 * @param ip 当前项目的ip
 * @param callbackName 回调函调namme
 *具体请看Xqgl里面的plupload页面的使用
 */
function gyPlupload(domId, callback, ip, callbackName) {
    var url = web_fileUpload_url;
    callbackName = "_" + new Date().getTime();
    if (window.location.port === "") {
        ip = ip || window.location.hostname;
    } else {
        ip = ip || window.location.hostname + ":" + window.location.port;
    }
    var funString = " var " + callbackName + " = " + callback;
    funString = strEnc(funString, "bai", "bin", "hao");
    var width = $("#"+domId).width();
    var height = $("#"+domId).height();
    $("#" + domId).html('<iframe style="width:99%;height:100%"  id="uploadFrame" src="' + url + '/fileManage/index.action?action=plupload&requestIp=' + ip + '&callback=' + funString + '&callbackName=' + callbackName + '" frameborder="0" scrolling="no"></iframe>');
}

/**
 * 服务器文件下载处理
 * @param url 访问服务器的url
 * @param path 文件服务器地址
 * @param filename 文件名称
 * @returns {string}
 */
function gyDownloadUrl(url,path,filename){
    var filePath = url+"/fileManage/downloadFile?path=" + encodeURI(encodeURI(path))+"&fileName="+encodeURI(encodeURI(filename));
    var htmlArr = ['<a href="', filePath, '"', '>', filename, '</a>'];
    return htmlArr.join("");
}
/**
 *
 * @param options
 * @constructor
 */
function DownLoadFile(options) {
    var config = $.extend(true, {method: 'post'}, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
}

/**
 * 组装前台button的方法，以便后面按钮赋权
 * @param text 按钮名称  如：新增，编辑，删除，查询等。。
 * @param icon 按钮所需要显示的图标
 * @param fun 按钮点击所需的事件
 * @returns {string} 返回相应的字符串组成
 * @constructor
 */
function gyAssembleButton(text,icon,fun){
    var str = "";
    str += "<span style=\"margin-right:5px\">"+
        "<a class=\"easyui-linkbutton l-btn l-btn-small\" data-options=\"iconCls:'"+icon+"'\" onclick=\""+fun+"\" group=\"\" id=\"\">"
        +"<span class=\"l-btn-left l-btn-icon-left\">"
        +"<span class=\"l-btn-text\">"+text+"</span>"
        +"<span class=\"l-btn-icon "+icon+"\">&nbsp;</span></span></a></span>"
    return str;
}