<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联系人管理(CTL template)</title>
<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
</head>
<body>
	$cycle{rowstyle:("odd","even")}

	$embed{"head.ctl"}

	<div style="float: left;width: 65%;height: 55px;">
		<div style="width: 100%;">
			<img src="images/user.gif"/><span style="font-size:24px;">你</span>可以从这里<a href="/full/contact"  title="为你添加新的联系人">增加</a>新的联系人。
		</div>
	</div>
	<br><br>
	
	<div style="float: left;width:60%;margin-right: 0px;clear: both;">
		<table cellpadding="0" cellspacing="0" width="100%">
			<thead style="font-weight: bold;">
				<tr style="height: 26px;background-image: url('/full/images/head.jpg');color: white;">
					<td style="width: 110px;">&nbsp;姓 名</td>
					<td style="width: 110px;">&nbsp;移动电话</td>
					<td style="width: 130px;">&nbsp;电子邮件</td>
					<td>&nbsp;住址</td>
					<td style="width: 80px;" ct:role="'admin','manager'">&nbsp;操作</td>
				</tr>
			</thead>
			<tbody>
				<tr style="cursor: default;height: 22px;" class="${rowstyle.next}" ct:for="contact : ctx.content">
					<td>&nbsp;<a href="contacts/${contact.id}">${contact.name}</a></td>
					<td>&nbsp;${contact.mobilePhone}</td>
					<td>&nbsp;${contact.email}</td>
					<td>&nbsp;${contact.address}</td>
					<td ct:role="'admin','manager'">&nbsp;<a href="contacts/${contact.id}!delete" onclick="return deleteContact('${contact.name}');">删除</a></td>
				</tr>
			</tbody>
		</table>
		
		<br/>
		<center>	
			$include{"pageNavigation.ctl",(pageUrl:"/full/contacts?page=",ctx:ctx)}
		</center>
	</div>

	<div style="width: 36%;float: right;">
		<ul style="margin-left: 2px;margin-top: -18px;">
			<li style="margin-top: 16px;">
				<h5>快速开始</h5>
				<img src="images/user.gif"/><span style="font-size:24px;">你</span>可以从这里快速<a href="#" title="为你添加新的联系人" onclick="createContact();void(0);return false;">增加</a>新的联系人。
			</li>
			<li style="margin-top: 16px;">
				<h5>操作小窍门</h5>
				<div>
					<div style="margin-left: 24px;">通过屏幕右上方的搜索栏快速检索你的联系人。</div>
					<div style="margin-left: 24px;">通过点击标题栏对你的查询结果进行排序。</div>
					<div style="margin-left: 24px;">按住Ctrl键可以多选。</div>
					<div style="margin-left: 24px;">通过拖拽的方式将选中的联系人，可以轻松的放入回收站。</div>
				</div>
			</li>
			<li style="margin-top: 16px;">
				<h5>友情链接</h5>
				<div>
					<div style="margin-left: 24px;"><a href="http://jrest4guice.googlecode.com">JRest4Guice - restful web service api for google guice</a></div>
					<div style="margin-left: 24px;"><a href="http://code.google.com/p/google-guice/">Google Guice - Google IOC framework</a></div>
					<div style="margin-left: 24px;"><a href="http://www.hibernate.org/410.html">Hibernate Search - Lucene for hibernate</a></div>
					<div style="margin-left: 24px;"><a href="http://labs.adobe.com/technologies/spry/home.html">Adobe Spry</a></div>
					<div style="margin-left: 24px;"><a href="http://jquery.com/">JQuery</a></div>
				</div>
			</li>
		</ul>
	</div>

	
	<div id="securityDiv" style="display: none;">
		<div style="width: 100%;height: 26px;background-image: url('images/head.jpg');"><img src="images/close.gif" style="cursor: pointer;margin-top: 4px;" onclick="closeLoginWindow();"></div>
		<iframe id="securityIframe" frameborder="0" width="280" height="200" scrolling="no"></iframe>
	</div>
	
	$embed{"foot.ctl"}
</body>
</html>