/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
window.onload = function(){
	var param = location.href;
	var index = param.indexOf("?");
	if(index != -1){
		param = param.substring(index+1);
		param = param.replace(/=/g,":").replace(/&/g,",").replace(/%27/g,"'");
		try{
			param = eval("({"+param+"})");
		}catch(e){}
		if(param.fileUrl){
			$("#imgeView").attr("src","upload/"+param.fileUrl+"?r="+Math.round(Math.random()*10000));
			$("#oldImgUrl").val(param.fileUrl);
			if(parent.onUpload)
				parent.onUpload.call(parent,param.fileUrl);
		}else{
			$("#imgeView").attr("src","images/head.gif");
		}
	}
}

function refreshProgress(){
	SpryExt.rest.doGet("monitor/",function(result){
		if(!result.errorMessage){
			updateProgress(result.content);
		}
	});
}

function updateProgress(uploadInfo){
	var enabled = uploadInfo.inProgress;
    if (uploadInfo.inProgress){
        var fileIndex = uploadInfo.fileIndex;
        var progressPercent = Math.ceil((uploadInfo.bytesRead / uploadInfo.totalSize) * 100);
        $('#progressBarText').html('上传进度: ' + progressPercent + '%');
        $('#progressBarBoxContent').css("width",parseInt(progressPercent * 2.7) + 'px');
        window.setTimeout('refreshProgress()', 1000);
    }

    $("#uploadForm input").each(function(){
    	this.disabled = enabled;
    });
}

function startProgress(){
    $('#progressBar').show();
    $('#progressBarText').html('上传进度: 0%');
    $('#progressBarBoxContent').css("width","0px");
    $("#uploadForm input[@type!='file']").each(function(){
    	this.disabled = true;
    });
    window.setTimeout("refreshProgress()", 1500);
    return true;
}

function onFileSelected(elem){
	$("#uploadForm").submit();
}
