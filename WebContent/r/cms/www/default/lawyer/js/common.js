
$(function() {
	$(".more_login").hover(function(){
		$(this).addClass("select_hover")
		
		},function(){
			$(this).removeClass("select_hover")
			})
			
			$(".top_nav_icon").hover(function(){
		$(this).addClass("select_nav")
		
		},function(){
			$(this).removeClass("select_nav")
			})
	
})

$(function(){	
	$('.ileft_a li').click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$('.panes>div:eq('+$(this).index()+')').show().siblings().hide();	
	})
})

$(function(){	
	$('.inews1_t li').click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$('.inews1_con>ul:eq('+$(this).index()+')').show().siblings().hide();	
	})
})




$(function(){	
	$('.inews2_t li').click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$('.inews2_con>.inews2_con_list:eq('+$(this).index()+')').show().siblings().hide();	
	})
})



$(function(){	
	$('.inews3_t li').click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$('.inews3_con>ul:eq('+$(this).index()+')').show().siblings().hide();	
	})
})



$(function(){
	$(".icase .icase_t ul li:first").addClass("hover");
	$(".icase .icase_box:first").addClass("show");
	$(".icase .icase_t ul li").each(function(i) {
        $(this).hover(function(){
			$(this).addClass("hover").siblings().removeClass("hover");
			$(".icase .icase_box").removeClass("show")
			$(".icase .icase_box:eq("+i+")").addClass("show");	
		})
    });
	
	
	})

$(function(){
	$(".inews4").each(function() {
        $(this).find(".inews4_t li").click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$(this).parents(".inews4").find('.inews4_con>ul:eq('+$(this).index()+')').show().siblings().hide();	
	})
    });	
	
})
