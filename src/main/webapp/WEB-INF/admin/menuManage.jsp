<%--
  Created by IntelliJ IDEA.
  User: think
  Date: 2018/1/5
  Time: 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>菜单管理页面</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/blog.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/common/public.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/common/datagridUtils.js"></script>
    <script type="text/javascript">
        $.extend({
            getUrlVars: function(){
                var vars = [], hash;
                var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                for(var i = 0; i < hashes.length; i++)
                {
                    hash = hashes[i].split('=');
                    vars.push(hash[0]);
                    vars[hash[0]] = hash[1];
                }
                return vars;
            },
            getUrlVar: function(name){
                return $.getUrlVars()[name];
            }
        });
        function formatFid(val,row){
            if(val) return val.name;
            return null;
        }
        function openMenuAddTab(){
            let selectedRow=$("#dg").treegrid("getSelected");
            let url = "${pageContext.request.contextPath}/admin/menu/addOrEditMenu.html";
            gyDivAddDialog("win",url,600,350,"新增菜单",function () {
                $("#fid").combotree({
                    url:'${pageContext.request.contextPath}/admin/menu/menuTree.do',
                    lines:true,
                    value:selectedRow?selectedRow.id:null
                });
                $('#createDate').datebox('setValue', myformatter(new Date()));
            },null,function (uuid) {
                gyFromSubmit("menuForm","${pageContext.request.contextPath}/admin/menu/saveMenu.do",function (data) {
                    $("#"+uuid).dialog("close");
                    $("#"+uuid).remove();
                    var par = data['fid'];
                    if(par) {
                        $("#dg").treegrid('reload', par);
                    }else {
                        $("#dg").treegrid('reload');
                    }
                })
            });

            //window.parent.openTab('新增菜单','menu/addMenu.html?id='+selectedRow?selectedRow.id:0,'icon-addMenu');
        }
        //得到当前日期
        var myformatter = function(date) {
            var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
            var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
                + (date.getMonth() + 1);
            return date.getFullYear() + '-' + month + '-' + day;
        };
        function openMenuModifyTab(){
            let selectedRows=$("#dg").treegrid("getSelections");
            let url = "${pageContext.request.contextPath}/admin/menu/addOrEditMenu.html";
            if(selectedRows.length!=1){
                TipUtils.bottomRight(null,"请选择一个要修改的菜单！");
                return;
            }
            let row=selectedRows[0];
            gyDivAddDialog("win",url,600,350,"修改菜单",function () {
                $("#fid").combotree({
                    url:'${pageContext.request.contextPath}/admin/menu/menuTree.do',
                    lines:true
                });
                $("#menuForm").form('load',row);
            },null,function (uuid) {
                gyFromSubmit("menuForm","${pageContext.request.contextPath}/admin/menu/saveMenu.do",function (data) {
                    $("#"+uuid).dialog("close");
                    $("#"+uuid).remove();
                    var par = data['fid'];
                    if(par) {
                       $("#dg").treegrid('reload', par);
                    }else {
                       $("#dg").treegrid('reload');
                    }
//                    $("#dg").treegrid('reload',row._parentId);
                })
            });
//            window.parent.openTab('修改博客','menu/modifyMenu.do?id='+row.id,'icon-writemenu');
        }
        function searchMenu(){
            $("#dg").treegrid('reload');
        }

        function deleteMenu(){
            var selectedRow=$("#dg").treegrid("getSelected");
            if(!selectedRow){
                $.messager.alert("系统提示","请选择一条要删除的数据！");
                return;
            }
            $.messager.confirm("系统提示","您确定要删除这条数据吗？",function(r){
                if(r){
                    $.post("${pageContext.request.contextPath}/admin/menu/delete.do",{id:selectedRow.id},function(result){
                        if(result.success){
                            TipUtils.bottomRight(null,"数据已成功删除！");
                            $("#dg").treegrid("reload");
                        }else{
                            AlertWarning("数据删除失败！");
                        }
                    },"json");
                }
            });
        }
        function dbcRow(row) {
            if(row['state']=='open'){
                $("#dg").treegrid('collapse',row['id']);
            }else if(row['state']=='closed'){
                $("#dg").treegrid('expand',row['id']);
            }
        }
    </script>
</head>
<body style="margin: 1px;">
<table id="dg" title="菜单管理" class="easyui-treegrid" fitColumns="true" pagination="true" rownumbers="true" fit="true" toolbar="#tb"
    data-options="url:'${pageContext.request.contextPath}/admin/menu/list.do',idField:'id',treeField:'text',onDblClickRow:dbcRow,pageSize:100,pageList:[100,200]">
    <thead>
    <tr>
        <th field="text" width="60" halign="center" align="left">菜单名称</th>
        <th field="iconCls" width="50" align="center">图标</th>
        <th field="url" width="50" align="center">url</th>
        <th field="createDate" width="50" align="center">创建日期</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openMenuAddTab()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
        <a href="javascript:openMenuModifyTab()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
        <a href="javascript:deleteMenu()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;菜单：&nbsp;<input type="text" id="s_name" size="20" onkeydown="if(event.keyCode==13) searchMenu()"/>
        <a href="javascript:searchMenu()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>
<div id="win"></div>
</body>
</html>
