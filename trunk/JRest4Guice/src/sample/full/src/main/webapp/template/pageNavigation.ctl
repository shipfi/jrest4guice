	<div style="margin-top:24px;">
		$init{max=12}
		$set{num=max/2}
		$set{hasPre=true}
		$set{hasNext=true}

		$set{start=ctx.pageIndex-num}
		$set{end=ctx.pageIndex+num}

		$set{pre=ctx.pageIndex-1}
		$if{pre<=0}
			$set{hasPre=false}
		$end
		
		$set{next=ctx.pageIndex+1}
		$if{next>ctx.pageCount}
			$set{hasNext=false}
		$end

		$if{start<=0}
			$set{start=1}
		$end
		$if{end<max}
			$set{end=max}
		$end
		$if{end>=ctx.pageCount}
			$set{end=ctx.pageCount}
		$end
		
		<span><a style="margin:0px 4px 0px 4px;" href="${pageUrl+pre}" ct:if="hasPre==true">上一页</a></span>
		<span ct:for="i:(start..end)"><a style="margin:0px 4px 0px 4px;color:red;" href="${pageUrl+i}" ct:if="ctx.pageIndex==i">${i}</a><a style="margin:0px 4px 0px 4px;" href="${pageUrl+i}" ct:if="ctx.pageIndex!=i">${i}</a></span>
		<span><a style="margin:0px 4px 0px 4px;" href="${pageUrl+next}" ct:if="hasNext==true">下一页</a></span>
	</div>