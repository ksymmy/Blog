<%--
  Created by IntelliJ IDEA.
  User: think
  Date: 2018/1/5
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="easyui-layout" style="width: 100%;height: 100%">
    <div data-options="region:'center'">
        <form id="menuForm" method="post">
            <div class="pops_list">
                <font>基本信息</font>
                <ul>
                    <li><b >上级菜单:</b>
                        <input type="hidden" name="id"/>
                        <input id="fid" name="_parentId" style="width: 120px;" data-options="required:false"/>
                    </li>
                    <li><b ><em></em>菜单名称</b>
                        <input class="easyui-textbox" style="width: 120px;" name="text" data-options="required:true"/>
                    </li>
                    <li><b ><em></em>url</b>
                        <input class="easyui-textbox" style="width: 120px;" name="url"/>
                    </li>
                    <li><b >图标</b>
                        <input class="easyui-textbox" name="iconCls" style="width: 120px;"/>
                    </li>
                    <li><b >创建时间:</b>
                        <input id="createDate" class="easyui-datebox" style="width: 120px;" data-options="required:true,formatter:myformatter" name="createDate" value="${createDate}"/>
                    </li>
                    <li><b >序号:</b>
                        <input class="easyui-numberbox" name="xh" style="width: 120px;"/>
                    </li>
                </ul>
            </div>
        </form>
    </div>
</div>