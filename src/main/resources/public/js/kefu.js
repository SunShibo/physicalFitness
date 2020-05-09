// JavaScript Document
var lastScrollY=0;

function heartBeat(){ 
	var diffY = 0;
	if (document.documentElement && document.documentElement.scrollTop)
		diffY = document.documentElement.scrollTop;
	else if (document.body)
		diffY = document.body.scrollTop;
	else
	{/*Netscape stuff*/}
	percent=.1*(diffY-lastScrollY); 
	if(percent>0)percent=Math.ceil(percent); 
	else percent=Math.floor(percent); 

	document.getElementById("lovexin12").style.top=parseInt(document.getElementById("lovexin12").style.top)+percent+"px";
	document.getElementById("lovexin14").style.top=parseInt(document.getElementById("lovexin12").style.top)+percent+"px";

	document.getElementById("lovexin22").style.top=parseInt(document.getElementById("lovexin22").style.top)+percent+"px";
	document.getElementById("lovexin24").style.top=parseInt(document.getElementById("lovexin24").style.top)+percent+"px";

	lastScrollY=lastScrollY+percent; 	
	
}

var suspendcode12="<div id='lovexin12' style='left:2px;position:absolute;top:180px;'><div id='img2'><a href='#'></a><span class='close'><a href='javascript:void(0);'  onclick='javascript:clo2()'></a></span></div></div>";
var suspendcode14="<div id='lovexin14' style='right:1px;position:absolute;top:180px;'><div id='img1' style='margin-top:180px'><a href='#' target='_blank'><img src='images/telphone.png' class='imgborder'/></a><span class='close'><a href='javascript:void(0);' onclick='javascript:clo1()' style='display:block'></a></span></div></div>";
document.write(suspendcode12); 
document.write(suspendcode14);

var suspendcode22="<div id='lovexin22' style='left:2px;position:absolute;top:180px;'><div id='img22'><a href='#'></a><span class='close'><a href='javascript:void(0);'  onclick='javascript:clo2()'></a></span></div></div>";
var suspendcode24="<div id='lovexin24' style='left:45px;position:absolute;top:180px;'><div id='img11' style='margin-top:180px'><a href='#' target='_blank'></a><span class='close'><a href='javascript:void(0);' onclick='javascript:clo1()' style='display:block'></a></span></div></div>";
document.write(suspendcode22); 
document.write(suspendcode24);

window.setInterval("heartBeat()",1);
