<%@ page contentType="text/html;charset=UTF-8"%>
<%-- <link href="${ctxStatic}/handsontable/handsontable.full.css" rel="stylesheet">
<script src="${ctxStatic}/handsontable/handsontable.full.js"></script>
<script src="${ctxStatic}/handsontable/plugins/pikaday.js" charset="utf-8"></script> --%>
<link href="${ctxStatic}/bootstrap/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
<link href="${ctxStatic}/bootstrap/plugins/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet"/>
<link href="${ctxStatic}/jquery/plugins/select2/css/select2.min.css" rel="stylesheet"/>
<script src="${ctxStatic}/bootstrap/plugins/bootstrap-editable/js/bootstrap-editable.min.js"></script>
<script src="${ctxStatic}/bootstrap/plugins/bootstrap-table/bootstrap-table.js"></script>
<script src="${ctxStatic}/bootstrap/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/editable/bootstrap-table-editable.min.js"></script>
<script src="${ctxStatic}/jquery/plugins/select2/js/select2.full.min.js"></script>
<script src="${ctxStatic}/jquery/plugins/select2/js/i18n/zh-CN.js"></script>
<script type="text/javascript">
$.fn.editable.defaults.mode = 'inline';//'poup';
$.fn.editable.defaults.showbuttons = false;
$.fn.editable.defaults.emptytext = '';
$.fn.editable.defaults.onblur = 'submit';
$.fn.editable.defaults.savenochange = true;
</script>