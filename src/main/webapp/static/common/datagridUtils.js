/**
 * Created by BaiBin on 2015/4/6.
 */
//var defaultView = WindowUtils.clone($.fn.datagrid.defaults.view);
var myView = $.extend({}, $.fn.datagrid.defaults.view, {
    onAfterRender: function (target) {
        //defaultView.onAfterRender.call(this,target);
        var opts = $(target).datagrid('options');
        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
        vc.children('div.datagrid-empty').remove();
        if (!$(target).datagrid('getRows').length) {
            var d = $('<div class="datagrid-empty"></div>').html(opts.emptyMsg || 'no records').appendTo(vc);
            d.css({
                position: 'absolute',
                left: 0,
                top: 50,
                width: '100%',
                textAlign: 'center'
            });
        }
    }
});
$.fn.datagrid.defaults.view = myView;
$.fn.datagrid.defaults.emptyMsg = "<span style='color:\"#0f0f0f\"'>暂无数据</span>";
var DatagridUtil = {
    /**
     * 我们传一个datagrid所在的tab的id进去,第二个参数是代表datagrid中的每行的主键
     * 返回这个datagrid中选中的行的id值
     * 注意：你要保证有且仅有一行被选中
     * @param datagridId
     * @param recordId
     * @returns {*}
     */
    getDatagridSelectedById: function (datagridId, recordId) {
        var selectROW = $("#" + datagridId).datagrid("getSelected");
        var id = '';
        if (selectROW) {
            id = selectROW[recordId];
        }
        return id;
    }
};
var TipUtils = {
    /**
     * 对提示框的封装
     * title：提示框的标题
     * msg：提示的内容。
     * icon:图标样式 可选值："error" "info" "question" "warning"
     * @param title
     * @param msg
     */
    topLeft: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'topLeft'});
    },

    topCenterError: function (title, msg, icon) {
        msg = msg || '操作失败，请稍后重试！';
        icon = icon || 'error';
        TipUtils.topCenter(title, msg, icon);
    },
    topCenter: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'topCenter'});
    },
    topRight: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'topRight'});
    },
    centerLeft: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'centerLeft'});
    },
    center: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'center'});
    },
    centerRight: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'centerRight'});
    },
    bottomLeft: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'bottomLeft'});
    },
    bottomCenter: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'bottomCenter'});
    },
    bottomRight: function (title, msg, icon) {
        if (!title) {
            title = '温馨提示';
        }
        if (!msg) {
            msg = '操作成功!';
        }
        $.messager.show({title: title, msg: msg, icon: icon, position: 'bottomRight'});
    }
};

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};
/**
 * 将yyyy-MM-dd格式字符串转换为date
 * @param target
 */
Date.prototype.parseForChina = function (target) {
    var regEx = new RegExp("\\-", "gi");
    target = target.replace(regEx, "/");
    var milliseconds = Date.parse(target);
    var dependedDate = new Date();
    dependedDate.setTime(milliseconds);
    return dependedDate;
};



