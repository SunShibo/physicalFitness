/*可以重复使用 切换卡*/
   function setTab(name, cursel, n) {
	    for (i = 1; i <= n; i++) {
	        var menu = document.getElementById(name + i); //定义变量menu等于li的id
	        var con = document.getElementById("con_" + name + "_" + i); //定义变量con等于隐藏的div的id
	        menu.className = i == cursel ? "hover" : ""; //如果i==cursel则样式为“hover”，否则不加任何样式
	        con.style.display = i == cursel ? "block" : "none"; //如果i==cursel则显示div隐藏层 否则不显示
};
};
	$('.user_up').hover(function(){
        	$('.user_list').show();
        },function(){
        	$('.user_list').hide();
        })


/*导航条选中效果*/
$(function (){
 $(".xk_ul li").each(function(index){                   
    $(this).click(function(){        
        $(".xk_ul li").removeClass("on");
        $(".xk_ul li").eq(index).addClass("on");
          });
    });
 });

$(function (){
 $(".ke_nav a").each(function(index){                   
    $(this).click(function(){        
        $(".ke_nav a").removeClass("cin");
        $(".ke_nav a").eq(index).addClass("cin");
          });
    });
 });

    
  