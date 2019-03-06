<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Java开源博客系统后台管理页面-Powered by ksymmy</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
    <style type="text/css">
        #accordion div a[name='c']:hover {
            background: #b7d2ff;
        }

        .a-bgc {
            background: #b7d2ff !important;
        }
    </style>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $("a[name='c']").on("click", function (e) {
                $("a[name='c']").removeClass("a-bgc");
                $(this).addClass("a-bgc");
            })
        });

        let url;

        function openTab(text, url, iconCls) {
            if ($("#tabs").tabs("exists", text)) {
                $("#tabs").tabs("select", text);
                let tab = $('#tabs').tabs('getSelected');
                $('#tabs').tabs('update', {
                    tab: tab,
                    options: {
                        content: "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/" + url + "'></iframe>"
                    }
                });
            } else {
                $("#tabs").tabs("add", {
                    title: text,
                    iconCls: iconCls,
                    closable: true,
                    selected: true,
                    style: {overflow: 'hidden'},
                    content: "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/" + url + "'></iframe>"
                });
            }
        }

        function openPasswordModifyDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "修改密码");
            url = "${pageContext.request.contextPath}/admin/blogger/modifyPassword.do?id=${currentUser.id}";
        }

        function modifyPassword() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    let newPassword = $("#newPassword").val();
                    let newPassword2 = $("#newPassword2").val();
                    if (!$(this).form("validate")) {
                        return false;
                    }
                    if (newPassword != newPassword2) {
                        $.messager.alert("系统提示", "确认密码输入错误！");
                        return false;
                    }
                    return true;
                },
                success: function (result) {
                    let rel = eval('(' + result + ')');
                    if (rel.success) {
                        $.messager.alert("系统提示", "密码修改成功，下一次登录生效！");
                        resetValue();
                        $("#dlg").dialog("close");
                    } else {
                        $.messager.alert("系统提示", "密码修改失败！");
                    }
                }
            });
        }

        function closePasswordModifyDialog() {
            resetValue();
            $("#dlg").dialog("close");
        }

        function resetValue() {
            $("#oldPassword").val("");
            $("#newPassword").val("");
            $("#newPassword2").val("");
        }

        function logout() {
            $.messager.confirm("系统提示", "您确定要退出系统吗？", function (r) {
                if (r) {
                    window.location.href = '${pageContext.request.contextPath}/admin/blogger/logout.html';
                }
            });
        }

        function refreshSystem() {
            $.post("${pageContext.request.contextPath}/admin/system/refreshSystem.do", {}, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "已成功刷新系统缓存！");
                } else {
                    $.messager.alert("系统提示", "刷新系统缓存失败！");
                }
            }, "json");
        }

    </script>
</head>
<body class="easyui-layout">
<div region="north" style="height: 78px;background-color: #E0ECFF">
    <table style="padding: 5px" width="100%">
        <tr>
            <td width="50%">
                <img alt="logo" src="${pageContext.request.contextPath}/static/images/logo.png"
                     style="max-height: 60px;">
            </td>
            <td valign="bottom" align="right" width="50%">
                <font size="3">&nbsp;&nbsp;<strong>欢迎：</strong>${currentUser.userName }</font>
                <a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'">安全退出</a>
            </td>
        </tr>
    </table>
</div>
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs" data-options="tools:'#tab-tools',onContextMenu:function(e,title,index) {
                e.preventDefault();
				$('#tabmenu').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
                $('#tabmenu div').unbind('click');
				$('#closeThis').click(function() {
				    $('#tabs').tabs('close',index);
				    $('#tabmenu').menu('hide');
				});
				$('#closeOther').click(function() {
				    let tabs = $('#tabs').tabs('tabs');
                    for(let i=tabs.length-1;i>=0;i--) {
                        if(i!=index) {
				            $('#tabs').tabs('close',i);
                        }
				    }
				    $('#tabmenu').menu('hide');
				});
				$('#closeAll').click(function() {
				    let tabs = $('#tabs').tabs('tabs');
				    for(let i=tabs.length-1;i>=0;i--) {
				        $('#tabs').tabs('close',i);
				    }
				    $('#tabmenu').menu('hide');
				});
				$('#updateThis').click(function() {
				    let tab = $('#tabs').tabs('getTab',index);
				    $('#tabs').tabs('update',{
				        tab:tab,
				        options:{
				        }
                    })
                    $('#tabmenu').menu('hide');
				});
				$('#undoThis').click(function() {
				    let tab = $('#tabs').tabs('getTab',index);
				    let src = tab.children().attr('src');
				     $('#tabmenu').menu('hide');
				    window.open(src);
				});
        }">
        <div title="首页" data-options="iconCls:'icon-home'">
            <div align="center" style="padding-top: 100px">
                <font color="red" size="5">欢迎访问本站</font>
                <p>ksymmy</p>
            </div>
        </div>
    </div>
</div>
<div region="west" style="width: 200px" title="导航菜单" split="true">
    <div id="accordion" class="easyui-accordion" data-options="fit:true,border:false">
        <c:forEach items="${menuList}" var="big" varStatus="state">
            <c:choose>
                <c:when test="${!state.last}">
                    <div title="${big.text}" data-options="iconCls:'${big.iconCls}'" style="padding: 10px;">
                        <c:forEach items="${big.children}" var="small">
                            <a name="c" href="javascript:openTab('${small.text}','${small.url}','${small.iconCls}')"
                               class="easyui-linkbutton" data-options="plain:true,iconCls:'${small.iconCls}'"
                               style="width: 150px;">${small.text}</a>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div title="${big.text}" data-options="iconCls:'${big.iconCls}'" style="padding: 10px;">
                        <c:forEach items="${big.children}" var="small">
                            <a name="c" href="javascript:openTab('${small.text}','${small.url}','${small.iconCls}')"
                               class="easyui-linkbutton" data-options="plain:true,iconCls:'${small.iconCls}'"
                               style="width: 150px;">${small.text}</a>
                        </c:forEach>
                        <a name="c" href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
                        <a name="c" href="javascript:refreshSystem()" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'icon-refresh'" style="width: 150px;">刷新系统缓存</a>
                        <a name="c" href="javascript:logout()" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">安全退出</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</div>
<div region="south" style="height: 28px;padding: 5px" align="center">
    Copyright © 2015-2018 个人开发 暂无版权
</div>

<div id="tab-tools">
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-help'"
       onclick=""></a>
</div>


<div id="tabmenu" class="easyui-menu" style="width:120px;">
    <div id="undoThis" data-options="iconCls:'icon-submit'">新页面打开</div>
    <div class="menu-sep"></div>
    <div id="closeThis" data-options="iconCls:'icon-closeThis'">关闭当前</div>
    <div id="closeOther" data-options="iconCls:'icon-cancel'">关闭其他</div>
    <div id="closeAll" data-options="iconCls:'icon-remove'">关闭全部</div>
    <div class="menu-sep"></div>
    <div id="updateThis" data-options="iconCls:'icon-reload'">刷新本页面</div>
</div>

<div id="dlg" class="easyui-dialog" style="width:400px;height:200px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="userName" name="userName" readonly="readonly"
                           value="${currentUser.userName }" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>新密码：</td>
                <td><input type="password" id="newPassword" name="newPassword" class="easyui-validatebox"
                           required="true" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>确认新密码：</td>
                <td><input type="password" id="newPassword2" name="newPassword2" class="easyui-validatebox"
                           required="true" style="width: 200px"/></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>