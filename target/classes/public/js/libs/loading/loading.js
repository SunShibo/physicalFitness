/**
 * 遮罩层加载
 */

// 显示遮罩层
	function showLoading(msg){
		$("body").mLoading({
		    text:msg,//加载文字，默认值：加载中...
		    html:false,//设置加载内容是否是html格式，默认值是false
		    content:"",//忽略icon和text的值，直接在加载框中显示此值
		    mask:true//是否显示遮罩效果，默认显示
		});
		
//		$("body").mLoading("show");
	}
	
	// 隐藏遮罩层
	function hideLoading(){
		
		$("body").mLoading("hide");
	}