jQuery(function ($) {
	
	
	"use strict";
	
    ////////////////////////////////////////////  
    // LOADING
    ///////////////////////////////////////////  

    window.onload = function () {
        document.getElementById('loading-mask').style.display = 'none';

    }



	
    ////////////////////////////////////////////  
    // HOVER ANIMATION
    ///////////////////////////////////////////  




    $(".how-work li img").hover(
        function () {
            $(this).addClass('wobble-vertical');
        },
        function () {
            $(this).removeClass('wobble-vertical');
        }
    );

    $("#our-clients td img").hover(
        function () {
            $(this).addClass('pop');
        },
        function () {
            $(this).removeClass('pop');
        }
    );





    ////////////////////////////////////////////  
    // NAVIGATION
    ///////////////////////////////////////////  


    $('#main-menu').onePageNav({
        begin: function () {
            console.log('start')
        },
        end: function () {
            console.log('stop')
        }
    });


    $('.slide-title').onePageNav({
        begin: function () {
            console.log('start')
        },
        end: function () {
            console.log('stop')
        }
    });
	
	
	
	 $('#main-menu li:first-child').addClass('first');
	  $('#main-menu li:last-child').addClass('last');



    $('[data-toggle="tooltip"]').tooltip();



	
    ////////////////////////////////////////////  
    // jPlayer Video & Audio Player
    ///////////////////////////////////////////  
	
	
	
    if (jQuery().jPlayer) {
        $('#jplayer_video').jPlayer({
            ready: function () {
                $(this).jPlayer("setMedia", {
                    m4v: "http://www.jplayer.org/video/m4v/Big_Buck_Bunny_Trailer.m4v",
                    ogv: "http://www.jplayer.org/video/ogv/Big_Buck_Bunny_Trailer.ogv",
                    webmv: "http://www.jplayer.org/video/webm/Big_Buck_Bunny_Trailer.webm",
                    poster: "http://www.jplayer.org/video/poster/Big_Buck_Bunny_Trailer_480x270.png"
                });
            },
            cssSelectorAncestor: '#jplayer_video_container',
            swfPath: "js",
            supplied: "webmv, ogv, m4v",
            size: {
                width: "640px",
                height: "360px",
                cssClass: "jp-video-360p"
            },
            smoothPlayBar: true,
            keyEnabled: true
        });

        $("#jplayer_audio").jPlayer({
            ready: function (event) {
                $(this).jPlayer("setMedia", {
                    m4a: "http://www.jplayer.org/audio/m4a/TSP-01-Cro_magnon_man.m4a",
                    oga: "http://www.jplayer.org/audio/ogg/TSP-01-Cro_magnon_man.ogg"
                });
            },
            cssSelectorAncestor: '#jplayer_audio_container',
            swfPath: "js",
            supplied: "m4a, oga",
            wmode: "window",
            smoothPlayBar: true,
            keyEnabled: true
        });

    }




    ///////////////////////////////////////////////////////////////////////////////////////////  
    // Slider  #home  Services  how-work  portfolio  facts  our-team  office-location
    /////////////////////////////////////////////////////////////////////////////////////////  


    function header() {
        var windowHeight = $(window).height();
        var slider = $("#sequence");


        slider.css("height", windowHeight + "px");


    }
    header();



    $('.flexslider').each(function () {
        var $this = $(this);
        var configs = new Array();
        configs['animation'] = ($this.data('animation') == 'undefined') ? 'slide' : $this.data('animation');
        configs['easing'] = ($this.data('easing') == 'undefined') ? 'easeInCircl' : $this.data('easing');
        configs['autoplay'] = ($this.data('autoplay') == true) ? true : false;
        configs['smoothH'] = ($this.data('smooth-height') == true) ? true : false;
        configs['control'] = ($this.data('control') == true) ? true : false;
        configs['direction'] = ($this.data('direction') == true) ? true : false;
        configs['aspeed'] = ($this.data('animation-speed') == 'undefined') ? 800 : $this.data('animation-speed');
        configs['sspeed'] = ($this.data('slideshow-speed') == 'undefined') ? true : $this.data('slideshow-speed');

        $this.imagesLoaded(function () {
            $this.flexslider({
                animation: configs['animation'],
                easing: configs['easing'],
                slideshow: configs['autoplay'],
                smoothHeight: configs['smoothH'],
                controlNav: configs['control'],
                directionNav: configs['direction'],
                slideshowSpeed: configs['sspeed'],
                animationSpeed: configs['aspeed'],
                prevText: '<i class="icomoon-arrow-left"></i>',
                nextText: '<i class="icomoon-arrow-right"></i>'
            });
        });
    });



	
	
	 /////////////////////////////////////
    // Flickr Feed
   /////////////////////////////////////
	
	
	
    // Get your flickr ID from: http://idgettr.com/
	

	
  var flickr_id = '71865026@N00';
  
   var  $flcr_feed

    $flcr_feed = $('#flickr-feed');
    if ($flcr_feed.length) {
        $('#flickr-feed').jflickrfeed({
            limit: $flcr_feed.data('items'),
            qstrings: {
                id: '71865026@N00'
            },
            itemTemplate: '<li><a href="{{image_b}}" rel="prettyPhoto[flickr]"><img src="{{image_s}}" alt="{{title}}" /><span><i class="icomoon-search"></i></span></a></li>',
            itemCallback: function () {
                $("a[rel='prettyPhoto[flickr]']").prettyPhoto({
                    changepicturecallback: function () {
                        $('.pp_pic_holder').show();
                    }
                });
            }
        });
    }





	
	 /////////////////////////////////////
    // Open sub menu on hover main menu
   /////////////////////////////////////
   
   
    $('.main-menu li').hoverIntent({
        // on menu mouse hover function handler
        over: function () {
            $(this).addClass('hover-item').children('ul').animate({
                'height': 'toggle'
            }, 300);
        },
        // mouse out handler
        out: function () {
            var $this = $(this),
                $sub = $this.children('ul');
            if ($sub.length) {
                $this.children('ul').animate({
                    'height': 'toggle'
                }, 200, function () {
                    $this.removeClass('hover');
                });
            } else {
                $this.removeClass('hover');
            }
        },
        // A simple delay, in milliseconds, before the "out" function is called
        timeout: 200
    });



	 /////////////////////////////////////
    //  Sly Scroll Slider
   /////////////////////////////////////


    // Portfolio
    var $frame = $('.portfolio-frame'),
        $wrap = $frame.parent();

    if ($frame.length) {
        var portfolio_slider = new Sly($frame, {
            horizontal: 1,
            itemNav: 'basic',
            smart: 1,
            mouseDragging: 1,
            touchDragging: 1,
            releaseSwing: 1,
            scrollBar: $wrap.find('.sly_scrollbar'),
            speed: 300,
            elasticBounds: 1,
            easing: 'easeOutExpo',
            dragHandle: 1,
            dynamicHandle: 1,
            clickBar: 1
        });
    }




    // Portfolio
    var $frame = $('.team-frame'),
        $wrap = $frame.parent();

    if ($frame.length) {
        var team_slider = new Sly($frame, {
            horizontal: 1,
            itemNav: 'basic',
            smart: 1,
            mouseDragging: 1,
            touchDragging: 1,
            releaseSwing: 1,
            scrollBar: $wrap.find('.sly_scrollbar'),
            speed: 300,
            elasticBounds: 1,
            easing: 'easeOutExpo',
            dragHandle: 1,
            dynamicHandle: 1,
            clickBar: 1,
        });
    }



	
	 /////////////////////////////////////
    //  jQuery prettyPhoto, lightbox
   /////////////////////////////////////
   
   
   
    $("a[rel^='prettyPhoto']").prettyPhoto({
        changepicturecallback: function () {
            $('.pp_pic_holder').show();
        }
    });
	
	$(".item-hover .vertical-center .btn").prettyPhoto({
        changepicturecallback: function () {
            $('.pp_pic_holder').show();
        }
    });
	
	


	
	 /////////////////////////////////////
    // | Responsive multi level menu
   // | Credits goes to: https:github.com/codrops/ResponsiveMultiLevelMenu
  // | Licensed under the MIT License
 /////////////////////////////////////
   
   
   
    $('#dl-menu').dlmenu({
        animationClasses: {
            classin: 'dl-animate-in-5',
            classout: 'dl-animate-out-5'
        }
    });


	
	 /////////////////////////////////////
    //  jQuery isotope
   /////////////////////////////////////

   $('.portfolio-filter  a').click();

    var $portfolio = $('.portfolio-slider'),
          $portfolio_items = $portfolio.find('.portfolio-item');

    $portfolio.imagesLoaded(function () {
		

        var wW = $(window).width()
          			
        if (wW < 800) {
			 var  rows = 1;
            $portfolio_items.css('width', wW - 30 + 'px');
        }
		
		else{   var  rows = 2;   }
	
		
        $portfolio.css('height', ((get_max($portfolio_items) + 40) * rows) + 'px');

        $portfolio.isotope({
            onLayout: function () {
                sticky_header();
            },
            itemSelector: $portfolio_items,
            layoutMode: 'fitColumns'
        }, function () {
            portfolio_slider.init();
            team_slider.init();
        });
		
		
		
		
		
    });



    /* Isotope filtering */
    $('.portfolio-filter a').on('click', function () {
        $(this).closest('ul').find('.btn-primary').removeClass('btn-primary').addClass('btn-default');
        $(this).addClass('btn-primary');
        var selector = $(this).attr('data-filter');
        $portfolio.isotope({
            filter: selector
        });
        return false;
    });



    $(window).on('smartresize', function () {
        sticky_header();
    });



	 /////////////////////////////////////
    //  Sticky Header
   /////////////////////////////////////
   
   
    if ($('body.sticky-header').length) {
        $(window).on('scroll', function () {
            var winH = $(window).scrollTop();
            var $pageHeader = $('.page-header');
            if (winH > 60) {
                $pageHeader.addClass('sticky');
            } else {
                $pageHeader.removeClass('sticky');
            }
        });
    }




    // set margin top for inner pages with fixed header
    function fix_content_margin() {
        var phH = $('.page-header').height();
        var mt = (phH > 100) ? phH : 100 - phH;
        $('.inner-page.sticky-header #main').css('margin-top', mt);
    }
    fix_content_margin();


    function sticky_header() {
        // Destory All waypoints
        $.waypoints('destroy');

        // Hashchange event
        onHashChange();

        fix_content_margin();

        // Change active class on scroll to sections
        $('body:not(.inner-page) .section').waypoint({
            handler: function (direction) {
                // add active class to reached waypoint
                var $active_section;
                $active_section = $(this);
                if (direction === 'up') $active_section = $active_section.prev();
                $('.menu').find('.active').removeClass('active');
                $('.menu').find('a[href="#' + $active_section[0].id + '"]').parent().addClass('active');
            },
            offset: '15%'
        });

        try {
            portfolio_slider.reload();
            team_slider.reload();
            tweet_slider_width();
            tweet_slider.reload();
        } catch (err) {}




	 /////////////////////////////////////
    //  Chars Start
   /////////////////////////////////////
   
   
   
   
   
   $('#portfolio').waypoint(function(direction) {



  function CharsStart() {


                $('.chart').easyPieChart({

                    barColor: false,
                    trackColor: false,
                    scaleColor: false,
                    scaleLength: false,
                    lineCap: false,
                    lineWidth: false,
                    size: false,
                    animate: 7000,


                    onStep: function (from, to, percent) {

                        $(this.el).find('.percent').text(Math.round(percent));



                    }
                });

            }




            setTimeout(CharsStart, 0);




});



	 /////////////////////////////////////
    //  animate elements when they are in viewport
   /////////////////////////////////////
   



        $('.noIE .animated:not(.animation-done)').waypoint(function () {

            var animation = $(this).data('animation');

            $(this).addClass('animation-done').addClass(animation);

        }, {
            triggerOnce: true,
            offset: '85%'
        });

    }



	
	
	 /////////////////////////////////////
    //  Hashchange & ScrollTo Plugin
   /////////////////////////////////////
   
   
   
   
    function onHashChange() {
        $(window).bind('hashchange', function (e) {
            e.preventDefault();
            try {
                var target = '#' + window.location.hash.substring(1),
                    $target = $(target);
            } catch (e) {
                // console.debug(e.message);
            }
            if ($target === undefined || !target || $target.length == 0) return false;

            $('body').scrollTo($target, 500, {
                easing: 'easeInQuad'
            }, function () {
                sticky_header();
            });
            return false;
        });
        $(window).trigger('hashchange');
        $('.menu li > a, .scrollto').on('click', function (e) {
            if ($(e.target) === undefined || $(e.target).length == 0) return false;
            $(window).unbind('hashchange');
            $('body').scrollTo($(this).attr('href'), 200, {
                easing: 'easeInQuad'
            });
            $(window).bind('hashchange');
        });
    }

	
	 /////////////////////////////////////
    //  Contact us form validation
   /////////////////////////////////////
   
   
   
   
    $('#contact-form').on('submit', function (e) {
        e.preventDefault();
        // we clear error messages
        $(this).find('.error').removeClass('error');
        $(this).find('.err_msg').fadeOut(200);

        // validate form
        var validation = validate_contact(e);

        for (var i = 0; i < validation.length; i++) {
            $(validation[i]).addClass('error');
        }

        if (validation.length) {
            $('body, html').animate({
                'scrollTop': $(validation[0]).offset().top - 100
            }, 'easeInCube', function () {
                $(this).select();
            });
            return false;
        } else {
            submit_form(e);
            return true;
        }
    });

    function validate_contact(e) {
        var $form = $(e.target);
        var rule, val, bad_fields = new Array();
        $form.find('input, textarea').each(function () {
            rule = $(this).data('validate');
            if (!rule) return;

            val = $(this).val();
            if (!val.match(rule)) {
                bad_fields.push(this);
            }
        });
        return bad_fields;
    }



	
	 /////////////////////////////////////
    //  Contact us form submit
   /////////////////////////////////////
   
   
   
   
    function submit_form(e) {
        var $form = $(e.target),
            $btn = $form.find('button'),
            btn_text = $btn.text();
        $.ajax({
            url: 'inc/phpmailer/contact.php',
            data: $form.serialize(),
            dataType: 'json',
            type: 'POST',
            beforeSend: function () {
                $('#contact_fail .alert-inner').empty();
                $('#contact_fail').hide();
                $btn.attr('disabled', 'disabled').addClass('btn-disabled').css('cursor', 'not-allowed').text('Sending...');
            },
            success: function (result) {
                if (typeof result.success == 'undefined') {
                    // form is not valid, display errors
                    for (var x in result) {
                        $('#contact_fail .alert-inner').append('<p>' + result[x] + '</p>');
                    }
                    $('#contact_fail').fadeIn();
                } else {
                    // form sent successfully and without errors
                    $('#contact_success').fadeIn(700, function () {
                        var $this = $(this);
                        setTimeout(function () {
                            $this.fadeOut();
                        }, 5000);
                    });
                }
            },
            complete: function () {
                $btn.removeAttr('disabled', 'disabled').removeClass('btn-disabled').css('cursor', 'pointer').html(btn_text);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                switch (jqXHR.status) {
                case 404:
                    alert("We're Sorry... The file you are looking for is not found :(");
                    break;
                case 500, 200:
                    $('#contact_fail .alert-inner').append("<p>Oops, something went wrong and we couldn't send your message :(</p>");
                    $('#contact_fail').fadeIn();
                    break;
                default:
                    console.log(jqXHR, textStatus, errorThrown);
                }
            }
        });
    }

    function get_max($el) {
        /* Get max height */
        var max = 0;
        $el.each(function () {
            var this_h = $(this).outerHeight();
            if (this_h > max) max = this_h;
        });
        return max;
    }
	
	
	

});