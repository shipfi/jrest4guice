function refreshProgress(){
	SpryExt.rest.doGet("resource/monitor/",function(result){
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
        $('#progressBarText').html('upload in progress: ' + progressPercent + '%');
        $('#progressBarBoxContent')[0].style.width = parseInt(progressPercent * 3.5) + 'px';
        window.setTimeout('refreshProgress()', 1000);
    }

    $("#uploadForm input").each(function(){
    	this.disabled = enabled;
    });
}

function startProgress(){
    $('#progressBar').show();
    $('#progressBarText').html('upload in progress: 0%');
    $("#uploadForm input[@type!='file']").each(function(){
    	this.disabled = true;
    });
    window.setTimeout("refreshProgress()", 1500);
    return true;
}
