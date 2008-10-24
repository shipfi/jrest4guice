	<div style="margin-top:24px;" ct:if="ctx.resultCount>0">
		@init{max=12}
		@set{num=max/2}
		@set{hasPre=true}
		@set{hasNext=true}

		@set{start=ctx.pageIndex-num}
		@set{end=ctx.pageIndex+num}

		@set{pre=ctx.pageIndex-1}
		@if{pre<=0}
			@set{hasPre=false}
		@end
		
		@set{next=ctx.pageIndex+1}
		@if{next>ctx.pageCount}
			@set{hasNext=false}
		@end

		@if{start<=0}
			@set{start=1}
		@end
		@if{end<max}
			@set{end=max}
		@end
		@if{end>=ctx.pageCount}
			@set{end=ctx.pageCount}
		@end
		
		<div style="text-align:left;margin:8px 0 16px 8px;"><image src="@{ctxPath}/images/search_result.gif">&nbsp;&nbsp;&nbsp;<b>搜索结果：</b>为您搜索到 <b><font color="red">@{ctx.resultCount}</font></b> 个结果，共 <b><font color="red">@{ctx.pageCount}</font></b> 页。</div>		
		<span><a style="margin:0px 4px 0px 4px;font-weight: bold;" href="@{ctxPath+pageUrl+pre}" ct:if="hasPre==true">上一页</a></span>
		<span ct:for="i:(start..end)"><a style="margin:0px 4px 0px 4px;color:red;" href="@{ctxPath+pageUrl+i}" ct:if="ctx.pageIndex==i"><b>@{i}</b></a><a style="margin:0px 4px 0px 4px;" href="@{ctxPath+pageUrl+i}" ct:if="ctx.pageIndex!=i">@{i}</a></span>
		<span><a style="margin:0px 4px 0px 4px;font-weight: bold;" href="@{ctxPath+pageUrl+next}" ct:if="hasNext==true">下一页</a></span>
	</div>