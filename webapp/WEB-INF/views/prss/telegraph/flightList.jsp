<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title>航班信息</title>
</head>
<body>

<script type="text/javascript">
	$(function(){
		var layer;
		var clickRowId = "";

		layui.use(['form','layer'],function(){
			layer = layui.layer;
		});
		jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
		jQuery.fn.bootstrapTable.columnDefaults.align = "center";
		jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
		//向导内下拉表
		var tableOptions = {
				url: ctx+"/telegraph/history/getFlightList", //请求后台的URL（*）
// 				url: ctx+"/telegraph/templ/getListData?sitaType="+sitaType + "&ids=" + ids, //请求后台的URL（*）
			    method: "get",					  				 //请求方式（*）
			    dataType: "json",
				striped: true,									 //是否显示行间隔色
			    cache: true,
// 			    search:true,
				queryParams: function (params) {
				        return {
				        	flightdate : $("#flightdate").val(),
				        	flightnumber : $("#flightnumber").val(),
				        	fiotype : $("#fiotype").val()
				        }
				},
			    columns: [
							{field: "ID", title: "序号",editable:false,
								formatter : function (value,row,index){
									return (index +1);
								}
							},
							{field: "FLIGHT_DATE", title: "航班日期",editable:false,},
							{field: "FLIGHT_NUMBER", title: "航班号",editable:false},
							{field: "AIRCRAFT_NUMBER", title: "机号",editable:false},
							{field: "IN_OUT_FLAG", title: "进出港标识",editable:false,
								formatter : function (value,row,index){
									var flag = '';
									if(row.IN_OUT_FLAG == 'D0'){
										flag = '出港';
									} else if(row.IN_OUT_FLAG == 'D1'){
										flag = '进出-出';
									} else if(row.IN_OUT_FLAG == 'A0'){
										flag = '进港';
									} else if(row.IN_OUT_FLAG == 'A1'){
										flag = '进出-进';
									} 
									return flag;
								}
							}
			    ],
			    onDblClickRow:onDblClickRow
			};
		function onDblClickRow(row,tr,field){
			
			var flightDate=row.FLIGHT_DATE;
			var flightNumber=row.FLIGHT_NUMBER;
			var aircraftNumber = row.AIRCRAFT_NUMBER;
			var fltId = row.FLTID;
			$.ajax({
				type:'post',
				data:{
					'id': $("#id").val(),
					'isHis': $("#isHis").val(),
					'fltId' : fltId,
					'flightDate': flightDate,
					'flightNumber' : flightNumber,
					'aircraftNumber' : aircraftNumber
				},
				async:false,
				url:ctx+"/telegraph/history/pigeonhole",
				dataType:"json",
				success:function(result){
					var res = result.result;
					if(res==true){
						parent.refresh();
				        var index = parent.layer.getFrameIndex(window.name);
				        parent.layer.close(index); //再执行关闭 
					} else {
						var result = "操作失败";
						layer.msg(result, {icon: 1,time: 1000});
					}  
				},
				error:function(msg){
					var result = "操作失败";
					layer.msg(result, {icon: 1,time: 1000});
				}
			});
			
		}	
		tableOptions.height = $("#baseTables").height();
		$("#mTable").bootstrapTable(tableOptions);
		$(".search").click(function(){
			$("#mTable").bootstrapTable('refresh',tableOptions);
		})
	});
</script>
	<div class="col-md-12" style="margin-top: 10px;">
			<input type="hidden" id="id" value="${id }"/>
			<input type="hidden" id="isHis" value="${isHis }"/>
			<div class="layui-inline">
				<label class="layui-form-label">航班日期</label>
				<div class="layui-input-inline">
					<input type='text' maxlength="20" name='flightdate' id='flightdate'
						placeholder='请选择日期' class='form-control' value="${time }"
						onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline">
					<input class="form-control" type="text" name="flightnumber" id='flightnumber' placeholder="航班号">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">进出港标识</label>
				<div class="layui-input-inline">
					<select name="fiotype" id="fiotype" class="form-control"
						style="width: 170px;">
						<option value="all">全部</option>
						<option value="A">进港</option>
						<option value="D">出港</option>
					</select>
				</div>
			</div>
			<div class="bs-bars pull-right">
					<div id="toolbar">
						<button type="button" class="btn btn-link search">查询</button>
					</div>
				</div>
		<div id="mTables">
			<table id="mTable"></table>
		</div>
		</div>
</body>
</html>