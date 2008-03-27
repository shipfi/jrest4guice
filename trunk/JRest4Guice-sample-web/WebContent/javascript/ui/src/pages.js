/**
 * 构造分页信息条
 * pageInfo 分页信息 {amount:总数,countPerPage:每页条数,index:当前页码}
 * var pageInfo = new Object();
 * pageInfo.amount = 100;//符合查询条件的记录总数
 * pageInfo.countPerpage = 20; //每页显示记录的条数
 * pageInfo.index = 1; //当前页
 *
 * msg 提示信息(如:'个联系人');
 * infoBar 分页信息的显示区域(html元素的ID)
 * navigations 分页的导航区域(html元素的ID)
 * goPage 执行分页操作的函数
 * 
 * 用法 buildPageInfoBar(pageInfo, "条记录", "infoBar", "navigation", loadData);
 **/
var buildPageInfoBar = function(pageInfo, msg, infoBar, navigations, goPage)
{
	var pageAccount = 9;
	if(pageInfo.countPerPage == null && pageInfo.countPerPage == 0) 
		 pageInfo.countPerPage = 1;  
	try{
		pageInfo.amount = parseInt(pageInfo.amount);
	}catch(exception)
	{
		pageInfo.amount = 0;	
	}	var page = Math.ceil(pageInfo.amount/pageInfo.countPerPage);
	document.getElementById(infoBar).innerHTML = "&nbsp;&nbsp;"+pageInfo.amount + msg+"，共" + page + "页";
	var navigation = document.getElementById(navigations);
	
	navigation.innerHTML = "";
	
	//  定制导航信息 ：上一页 1 2 3 下一页

	//定制上一页
	var textPerPage = document.createElement("span"); 
	textPerPage.style.width = "45px";
	textPerPage.innerHTML = "上一页";
	textPerPage.className="pageClass";
	if(pageInfo.index > 1) {
		textPerPage.style.visibility="visible";
		textPerPage.onclick =  function(){goPage(parseInt(this.toString()));
			}.bind(pageInfo.index -1); 
	}else{textPerPage.style.visibility="hidden";};
	navigation.appendChild(textPerPage);
	//定制 1 2 3 4 5 6
	var pagespan = document.createElement("span");
	pagespan.className="pageClass";
		for(var i=0;i<pageAccount;i++)
		{
			if(i>=page) break;
			if(pageInfo.index<5 || page<=pageAccount)
				pageid = i+1;
			else if(pageInfo.index >=5 && pageInfo.index<page-4 && page>pageAccount)
				pageid = pageInfo.index + i - 4;
			else if(pageInfo.index>=page-4 && pageInfo.index >=5 && page>pageAccount)
				pageid = pageInfo.index + i -4 -(pageInfo.index-page+4);
			pagespanclone = pagespan.cloneNode(true);
			pagespanclone.innerHTML = pageid;
			if(pageid == pageInfo.index)
				pagespanclone.className="pageClassd";
			else
				pagespanclone.onclick = function(){
					goPage(parseInt(this.toString()));
				}.bind(pageid)
			navigation.appendChild(pagespanclone);
	  }
	  //定制下一页
	  var textNextPage = document.createElement("span");
	  textNextPage.style.width = "45px";
	  textNextPage.innerHTML = "下一页";
	  textNextPage.className="pageClass";
	  if(pageInfo.index < page )
	  {
	  	  textNextPage.style.visibility="visible";
	  	  textNextPage.onclick = function(){
						goPage(parseInt(this.toString()));
					}.bind(pageInfo.index + 1);
	  }else{textNextPage.style.visibility="hidden";};
  	  navigation.appendChild(textNextPage);
 }