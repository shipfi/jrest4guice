<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>联系人管理(CTL template)</title>
	<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
	<script>
		window.onload = function(){
			IFrameUtil.subscribeEvent("onLogin",window,function(param){
				window.location = window.location; 
			});
			var _table = new TableDecorator(null,"contactTable");
			_table.onChecked=_table.onCheckedAll=_table.onUnCheckedAll=function(){
				var ids = contactTable_decorator.getCheckedIds();
				if(ids.length>0){
					$("#delSpan").show();
					$("#delLink").attr("href","contacts/"+ids.join(",")+"!delete");
				}else{
					$("#delSpan").hide();
				}
			};

			var showCkb = true;
			@isLogin{false}
				showCkb = false;
			@end
			
			var checkedIds = [];
			@if{xctx.contactId != null}
				checkedIds.push('@{xctx.contactId}');
			@end
			
			_table.decorateRow({showCheckBox:showCkb,checkedIds:checkedIds});
		}
		
		function deleteContact(){
			if(!window.confirm("您确定要删除当前已选择的联系人吗?")){
				return false;
			}
			return true;
		}
	</script>
</head>
<body>
	<!-- 定义奇偶行样式 -->
	@cycle{rowstyle:("odd","even")}

	<!-- 嵌入页头 -->
	@embed{"head.ctl"}

	<div style="float: left;width: 65%;height: 55px;">
		<div style="width: 100%;">
			<img src="images/user.gif"/><span style="font-size:24px;">你</span>可以从这里<a href="/full/contact"  title="为你添加新的联系人">增加</a>新的联系人<span ct:role="'admin','manager'"><span id="delSpan" style="display:none;">，或者<a id="delLink" href="contacts/!delete" onclick="return deleteContact();">删除</a>当前已选择的联系人</span></span>。
		</div>
	</div>
	<br><br>
	
	<div style="float: left;width:60%;margin-right: 0px;clear: both;">
		<table id="contactTable" cellpadding="0" cellspacing="0" width="100%">
			<thead style="font-weight: bold;">
				<tr style="height: 26px;background-image: url('/full/images/head.jpg');color: white;">
					<td style="width: 110px;">&nbsp;姓 名</td>
					<td style="width: 110px;">&nbsp;移动电话</td>
					<td style="width: 130px;">&nbsp;电子邮件</td>
					<td>&nbsp;住址</td>
				</tr>
			</thead>
			<tbody>
				<tr ct:for="contact : ctx.content"
					rowId="@{contact.id}" 
					class="@{rowstyle.next}" style="cursor: default;height: 22px;">
					<td>&nbsp;<a href="contacts/@{contact.id}">@{contact.name}</a></td>
					<td>&nbsp;@{contact.mobilePhone}</td>
					<td>&nbsp;@{contact.email}</td>
					<td>&nbsp;@{contact.address}</td>
				</tr>
			</tbody>
		</table>
		
		<br/>
		<center>	
			<!-- 插入分页处理 -->
			@include{"pageNavigation.ctl",(pageUrl:"/full/contacts?page=",ctx:ctx)}
		</center>
	</div>

	<div style="width: 36%;float: right;">
		<ul style="margin-left: 2px;margin-top: -18px;">
			<li style="margin-top: 16px;">
				<h5>快速开始</h5>
				<img src="images/user.gif"/><span style="font-size:24px;">你</span>可以从这里<a href="/full/contact"  title="为你添加新的联系人">增加</a>新的联系人。
			</li>
			<li style="margin-top: 16px;">
				<h5>友情链接</h5>
				<div>
					<div style="margin-left: 24px;"><a href="http://jrest4guice.googlecode.com">JRest4Guice - restful web service framework for google guice</a></div>
					<div style="margin-left: 24px;"><a href="http://code.google.com/p/google-guice/">Google Guice - Google IOC framework</a></div>
					<div style="margin-left: 24px;"><a href="http://www.hibernate.org/410.html">Hibernate Search - Lucene for hibernate</a></div>
					<div style="margin-left: 24px;"><a href="http://www.commontemplate.org/zh/index.html">CommonTemplate(CTL)</a></div>
				</div>
			</li>
		</ul>
	</div>
	
	<div id="securityDiv" style="display: none;">
		<div style="width: 100%;height: 26px;background-image: url('images/head.jpg');"><img src="images/close.gif" style="cursor: pointer;margin-top: 4px;" onclick="closeLoginWindow();"></div>
		<iframe id="securityIframe" frameborder="0" width="280" height="200" scrolling="no"></iframe>
	</div>
	
	<!-- 嵌入页脚 -->
	@embed{"foot.ctl"}
</body>
</html>