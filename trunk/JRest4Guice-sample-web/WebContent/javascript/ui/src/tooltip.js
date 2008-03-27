/*
 +-------------------------------------------------------------------+
 |                   J S - T O O L T I P   (v2.0)                    |
 |                                                                   |
 | Copyright Gerd Tentler               www.gerd-tentler.de/tools    |
 | Created: Feb. 15, 2005               Last modified: Feb. 10, 2007 |
 +-------------------------------------------------------------------+
 | This program may be used and hosted free of charge by anyone for  |
 | personal purpose as long as this copyright notice remains intact. |
 |                                                                   |
 | Obtain permission before selling the code for this program or     |
 | hosting this software on a commercial website or redistributing   |
 | this software over the Internet or in any other medium. In all    |
 | cases copyright must remain intact.                               |
 +-------------------------------------------------------------------+

======================================================================================================

 This script was tested with the following systems and browsers:

 - Windows XP: IE 6, NN 7, Opera 7 + 9, Firefox 2
 - Mac OS X:   IE 5, Safari 1

 If you use another browser or system, this script may not work for you - sorry.

------------------------------------------------------------------------------------------------------

 USAGE:

 Use the toolTip-function with mouse-over and mouse-out events (see example below).

 - To show a tooltip, use this syntax: toolTip(text, width in pixels, opacity in percent)
   Note: width and opacity are optional

 - To hide a tooltip, use this syntax: toolTip()

------------------------------------------------------------------------------------------------------

 EXAMPLE:

 <a href="#" onMouseOver="toolTip('Just a test', 150)" onMouseOut="toolTip()">some text here</a>

======================================================================================================
*/

var OP = (navigator.userAgent.indexOf('Opera') != -1);
var IE = (navigator.userAgent.indexOf('MSIE') != -1 && !OP);
var GK = (navigator.userAgent.indexOf('Gecko') != -1);
var SA = (navigator.userAgent.indexOf('Safari') != -1);
var DOM = document.getElementById;

var tooltip = null;

function TOOLTIP() {
//----------------------------------------------------------------------------------------------------
// Configuration
//----------------------------------------------------------------------------------------------------
  // this.width = 200;                     // width (pixels)
  this.bgColor = "#fffbd0";             // background color
  this.textFont = "Lucida Grande";      // text font family
  this.padding = "7";
  this.textSize = 12;                   // text font size (pixels)
  this.textColor = "#5e5811";           // text color
  this.border = "1px dotted #e1db9a";   // border (CSS spec: size style color, e.g. "1px solid #D00000")
  this.opacity = 100;                    // opacity (0 - 100); doesn't work with all browsers
  this.cursorDistance = 5;              // distance from mouse cursor (pixels)

  // don't change
  this.text = '';
  this.height = 0;
  this.obj = null;
  this.active = false;

//----------------------------------------------------------------------------------------------------
// Methods
//----------------------------------------------------------------------------------------------------
  this.create = function() {
    if(!this.obj) this.init();

    var s = (this.textFont ? 'font-family:' + this.textFont + '; ' : '') +
            (this.textSize ? 'font-size:' + this.textSize + 'px; ' : '') +
            (this.border ? 'border:' + this.border + '; ' : '') +
            (this.textColor ? 'color:' + this.textColor + '; ' : '');

    var t = '<table border=0 cellspacing=0 cellpadding='+this.padding+' width=' + this.width + '><tr>' +
            '<td align=left' + (s ? ' style="' + s + '"' : '') + '>' + this.text +
            '</td></tr></table>';

    if(DOM || IE) this.obj.innerHTML = t;
    if(DOM) this.height = this.obj.offsetHeight;
    else if(IE) this.height = this.obj.style.pixelHeight;
    if(this.bgColor) this.obj.style.backgroundColor = this.bgColor;

    this.setOpacity();
    this.move();
    this.show();
  }

  this.init = function() {
    if(DOM) this.obj = document.getElementById('ToolTip');
    else if(IE) this.obj = document.all.ToolTip;
  }

  this.move = function() {
    var winX = getWinX() - (((GK && !SA) || OP) ? 17 : 0);
    var winY = getWinY() - (((GK && !SA) || OP) ? 17 : 0);
    var x = mouseX;
    var y = mouseY;

    if(x + this.width + this.cursorDistance > winX + getScrX())
      x -= this.width + this.cursorDistance;
    else x += this.cursorDistance;

    if(y + this.height + this.cursorDistance > winY + getScrY())
      y -= this.height;
    else y += this.cursorDistance;

    this.obj.style.left = x + 'px';
    this.obj.style.top = y + 'px';
  }

  this.show = function() {
    this.obj.style.visibility = 'visible';
    this.obj.style.zIndex = 500;
    this.active = true;
  }

  this.hide = function() {
    this.obj.style.visibility = 'hidden';
    this.obj.style.zIndex = -1;
    this.active = false;
  }

  this.setOpacity = function() {
    this.obj.style.filter = 'alpha(opacity=' + this.opacity + ')';
    this.obj.style.mozOpacity = '.1';
    if(this.obj.filters) this.obj.filters.alpha.opacity = this.opacity;
    if(!document.all && this.obj.style.setProperty)
      this.obj.style.setProperty('-moz-opacity', this.opacity / 100, '');
  }
}

//----------------------------------------------------------------------------------------------------
// Global functions
//----------------------------------------------------------------------------------------------------
function getScrX() {
  var offset = 0;
  if(window.pageXOffset)
    offset = window.pageXOffset;
  else if(document.documentElement && document.documentElement.scrollLeft)
    offset = document.documentElement.scrollLeft;
  else if(document.body && document.body.scrollLeft)
    offset = document.body.scrollLeft;
  return offset;
}

function getScrY() {
  var offset = 0;
  if(window.pageYOffset)
    offset = window.pageYOffset;
  else if(document.documentElement && document.documentElement.scrollTop)
    offset = document.documentElement.scrollTop;
  else if(document.body && document.body.scrollTop)
    offset = document.body.scrollTop;
  return offset;
}

function getWinX() {
  var size = 0;
  if(window.innerWidth)
    size = window.innerWidth;
  else if(document.documentElement && document.documentElement.clientWidth)
    size = document.documentElement.clientWidth;
  else if(document.body && document.body.clientWidth)
    size = document.body.clientWidth;
  else size = screen.width;
  return size;
}

function getWinY() {
  var size = 0;
  if(window.innerHeight)
    size = window.innerHeight;
  else if(document.documentElement && document.documentElement.clientHeight)
    size = document.documentElement.clientHeight;
  else if(document.body && document.body.clientHeight)
    size = document.body.clientHeight;
  else size = screen.height;
  return size;
}

function getMouseXY(e) {
  if(e && e.pageX != null) {
    mouseX = e.pageX;
    mouseY = e.pageY;
  }
  else if(event && event.clientX != null) {
    mouseX = event.clientX + getScrX();
    mouseY = event.clientY + getScrY();
  }
  if(mouseX < 0) mouseX = 0;
  if(mouseY < 0) mouseY = 0;
  if(tooltip && tooltip.active) tooltip.move();
}

function toolTip(text, width, opacity, padding, bgcolor, color, font, cursor, border) {
  if(text) {
    tooltip = new TOOLTIP();
    tooltip.text = text;
    if(width) tooltip.width = width;
    if(opacity) tooltip.opacity = opacity;
	if(padding) tooltip.padding = padding;
	if(bgcolor) tooltip.bgColor = bgcolor;
	if(border) tooltip.border = border;
	if(cursor) tooltip.cursorDistance = cursor;
	if(color) tooltip.textColor = color;
	if(font) tooltip.textFont = font;
    tooltip.create();
  }
  else if(tooltip) tooltip.hide();
}

//----------------------------------------------------------------------------------------------------
// Build tooltip box
//----------------------------------------------------------------------------------------------------
document.write('<div id="ToolTip" style="position:absolute; visibility:hidden"></div>');

//----------------------------------------------------------------------------------------------------
// Event handlers
//----------------------------------------------------------------------------------------------------
var mouseX = mouseY = 0;
document.onmousemove = getMouseXY;

//----------------------------------------------------------------------------------------------------


/* 9rules zoom JS */
var xmlhttpZ = false;
function createXMLHTTPZ() {
	/* Thank you Jibbering.com */
	/*@cc_on @*/
	/*@if (@_jscript_version >= 5)
	// JScript gives us Conditional compilation, we can cope with old IE versions.
	// and security blocked creation of the objects.
	 try {
	  xmlhttpZ = new ActiveXObject("Msxml2.XMLHTTP");
	 } catch (e) {
	  try {
	   xmlhttpZ = new ActiveXObject("Microsoft.XMLHTTP");
	  } catch (E) {
	   xmlhttpZ = false;
	  }
	 }
	@end @*/
	if (!xmlhttpZ && typeof XMLHttpRequest!='undefined') {
		try {
			xmlhttpZ = new XMLHttpRequest();
		} catch (e) {
			xmlhttpZ=false;
		}
	}
	if (!xmlhttpZ && window.createRequest) {
		try {
			xmlhttpZ = window.createRequest();
		} catch (e) {
			xmlhttpZ=false;
		}
	}
}

function loadIntoPageZ(element_id, fragment_url) {

	createXMLHTTPZ();
		
	/* Thank you Simon Willison */
	var elementZ = document.getElementById(element_id);
    xmlhttpZ.onreadystatechange = function() {
        if (xmlhttpZ.readyState == 4 && xmlhttpZ.status == 200) {

			document.getElementById('zoom_inner').innerHTML = xmlhttpZ.responseText;
        }
    }
	
	xmlhttpZ.open("GET", fragment_url, true);

    xmlhttpZ.send(null);
}

/* Various parts of the code courtesy Quirksmode, they rock */
function getPageSize(){
	
	var xScroll, yScroll;
	
	if (window.innerHeight && window.scrollMaxY) {	
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
		yScroll = document.body.scrollHeight;
	} else {
		yScroll = document.body.offsetHeight;
	}
	
	var windowHeight;
	
	if (self.innerHeight) {	// all except Explorer
		windowHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
		windowHeight = document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowHeight = document.body.clientHeight;
	}	
	
	if(yScroll < windowHeight){
		pageHeight = windowHeight;
	} else { 
		pageHeight = yScroll;
	}
	return pageHeight;
}


function getScreenCenterY() {  
var y = 0;  
y = getScrollOffset()+(getInnerHeight()/2);  
return(y);  
}  
  
function getScreenCenterX() {  
return(document.body.clientWidth/2);  
}  
  
function getInnerHeight() {  
var y;  
if (self.innerHeight) 
{  
y = self.innerHeight;  
}  
else if (document.documentElement &&  
document.documentElement.clientHeight)  
{  
y = document.documentElement.clientHeight;  
}  
else if (document.body) 
{  
y = document.body.clientHeight;  
}  
return(y);  
}  
  
function getScrollOffset() {  
var y;  
if (self.pageYOffset)
{  
y = self.pageYOffset;  
}  
else if (document.documentElement && 
document.documentElement.scrollTop)  
{  
y = document.documentElement.scrollTop;  
}  
else if (document.body)
{  
y = document.body.scrollTop;  
}  
return(y);  
}  

function showZoom( t, zid ) {
	
	var type = t;
	var zoom_id = zid;
	
	posY = getScreenCenterY();
	bgheight = getPageSize();
		
	if( document.getElementById('zoom_inner').innerHTML != "" ) document.getElementById('zoom_inner').innerHTML = "";
	document.getElementById('zoom_inner').innerHTML = "<p style='text-align: center;'><img src='http://9rules.com/styles/ali_images/zoomload.gif' /></p>";

	var newpos = posY - 220;

	document.getElementById('zoom_background').style.height = bgheight + "px";		
	document.getElementById('zoom_inner').style.marginTop = newpos + "px";
	document.getElementById('zoom_inner').style.display = "block";
	document.getElementById('zoom_background').style.display = "block";
	
	loadIntoPageZ( 'zoom_inner','http://9rules.com/browse/ali2_zoom_loader.php?t='+type+'&zid='+zoom_id+'' );
		
}

function hideZoom() {
	document.getElementById('zoom_background').style.display = "none";
	document.getElementById('zoom_inner').style.display = "none";
}


function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	return curtop;
}


