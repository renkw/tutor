// JavaScript Document
$(function(){
			$('#focusPic li').eq(0).show();
			$('#pic_txt p').eq(0).show();
			var len = $('#num span').length;
			var index= 0;
			//数字索引显示图片
			$('#num span').mouseover(function(){
				index = $('#num span').index(this);
				showImg(index);
			});
			//鼠标经过图片区域停止播放
			$('#focus_pic_box').hover(function(){
				if(palyImg){
					clearInterval(palyImg);
				}
			},function(){
				palyImg = setInterval(function(){
					showImg(index);
					index++;
					if(index==len) {index=0}
				},3000);	
			});
			//自动播放
			var palyImg = setInterval(function(){
				showImg(index);
				index++;
				if(index==len) {index=0}
			},3000);
			function showImg(i){
				$('#focusPic li').eq(i).stop(true,true).fadeIn().siblings().fadeOut();
				$('#pic_txt p').eq(i).stop(true,true).fadeIn().siblings().fadeOut();
				$('#num span').eq(i).addClass('on').siblings().removeClass('on');
			}
		})