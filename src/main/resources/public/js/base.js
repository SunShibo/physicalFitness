	$(function(){
		 $(".sel2").select2({
        minimumResultsForSearch: -1
   });  
   
   $(".seloption").select2(); 
   
        $(".bgblue").click(function(){
        	 $(this).toggleClass("is-active");
            if($(this).hasClass('is-active')){
            	 $(this).html("已赞");
            }else{
            	$(this).html("赞一个");
           
          };
        });
         $(".bgyellow").click(function(){
        	 $(this).toggleClass("icon_cllect1");
            if($(this).hasClass('icon_cllect1')){
            	 $(this).html("已收藏");
            }else{
            	$(this).html("收藏");
           
          };
        });
        
//     $(".hcolse").click(function() {
//      $(this).text($(".iproces").is(":hidden") ? "收起" : "展开流程图");
//      $(".iproces").slideToggle(1500);
//      $(".hcolse").toggleClass("idown");
//      $(".hcolse").toggleClass("posup");
//      
//  });


$('.hcolse').click(function(){
	$('.zk_task').removeClass('disn');
	$(".iproces").addClass("disn");
});
$('.idown').click(function(){
	$('.iproces').removeClass('disn');
	$(".zk_task").addClass("disn");
});

    $(".idown").parent('').addClass("bg_line");
     $('.progressbar').each(function(index, el) {
        var num = $(this).find('span').text();
        $(this).addClass('progressbar-' + num);
    });
    $('.pornumCont').each(function(index, el) {
        var num1 = $(this).find('.pnum').text();
        var num2=5-num1;
        $('.pronum').addClass('pronum-' + num2);
    });
    
    });
