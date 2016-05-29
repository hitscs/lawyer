
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



                    $(function(){
                        $('.lawyer_ban .lawyer_ban_pic ul').width(470*$('.lawyer_ban .lawyer_ban_pic li').length+'px');
                        $(".lawyer_ban .lawyer_ban_tab li").mouseover(function(){
                            $(this).addClass('on').siblings().removeClass('on');
                            var index = $(this).index();
                            number = index;
                            var distance = -470*index;
                            $('.lawyer_ban .lawyer_ban_pic ul').stop().animate({
                                left:distance
                            });
                        });
                        
                        var auto = 1;  //等于1则自动切换，其他任意数字则不自动切换
                        if(auto ==1){
                            var number = 0;
                            var maxNumber = $('.lawyer_ban .lawyer_ban_tab li').length;
                            function autotab(){
                                number++;
                                number == maxNumber? number = 0 : number;
                                $('.lawyer_ban .lawyer_ban_tab li:eq('+number+')').addClass('on').siblings().removeClass('on');
                                var distance = -470*number;
                                $('.lawyer_ban .lawyer_ban_pic ul').stop().animate({
                                    left:distance
                                });
                            }
                            var tabChange = setInterval(autotab,3000);
                            //鼠标悬停暂停切换
                            $('.lawyer_ban').mouseover(function(){
                                clearInterval(tabChange);
                            });
                            $('.lawyer_ban').mouseout(function(){
                                tabChange = setInterval(autotab,3000);
                            });
                          }  
                    });



$(function(){
					$(".droplist").each(function() {
                        
						$(this).find("p").click(function(){
							$(".droplist ul").hide();
							
						var ul = $(this).parent(".droplist").find("ul");
						if(ul.css("display")=="none"){
							ul.slideDown("fast");
						}else{
							ul.slideUp("fast");
						}
						});
						$(this).find("ul li a").click(function(){
							var txt = $(this).text();
							$(this).parents(".droplist").find("p").html(txt);
							var value = $(this).attr("rel");
							$(this).parents(".droplist").find("ul").hide();
							
						});
						});
					
					
				});
				
				
				
				
				
$(function(){
	$(".lawyer_box .lawyer_box_t").each(function() {
        $(this).find("ul li").click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$(this).parents(".lawyer_box").find('.lawyer_con_tab:eq('+$(this).index()+')').show().siblings().hide();	
	})
    });	
	
})






$(function(){
	$(".lawyer_news").each(function() {
        $(this).find(".lawyer_tt ul li").click(function(){
		$(this).addClass('hit').siblings().removeClass('hit');
		$(this).parents(".lawyer_news").find('.lawyer_news_con>ul:eq('+$(this).index()+')').show().siblings().hide();	
	})
    });	
	
})


$(function(){

		 $("#city").click(function() {
            $(this).next().find('.city_text').fadeToggle(200);
        })
		$("#close").click(function() {
            $(this).parent('.city_text').fadeOut(200)
        })
		
		$(".more").click(function() {
            if ($(this).hasClass('sm_more')) {
                $(this).removeClass('sm_more').html("展开<em></em>");
                $(".all_law").hide();
                $(".hot_a").slideDown(0);
            } else {
                $(this).addClass('sm_more').html("缩回<em></em>");
                $(".all_law").slideDown(0);
                $(".hot_a").hide();
            }
        })
		
		})
		
		
		$(function(){
			
			
			
			$("#sort").click(function(){
				$("#sel_sort").show();
				$(".OpenDialogBox").css("top",($(window).height()-$(".OpenDialogBox").height())/2);
				})
			$("#sel_sort .DialogClose,.OpenDialogBg").click(function(){
				$("#sel_sort").hide();
				})
			})
			
			
			$(function(){
					$(".consult_d").find(".consult_d_t ul li").click(function(){
					$(this).addClass('hit').siblings().removeClass('hit');
					$(".consult_d").find('.consult_d_con>ul:eq('+$(this).index()+')').show().siblings().hide();	
				})
	
			})