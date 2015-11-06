
$('#div6').bind("mouseover", function () { $('#div6').css("background", "url(/site_media/images/t6.jpg)"); }).bind("mouseout", function () { $('#div6').css("background", ""); }).bind("click", function () { Bgmove();});
$('#div1').bind("mouseover", function () {

    if ($("#div1").attr("class") == 'div_3') { 
      return; 
    }; 
    $("#div1").attr("class", "div_2"); 
    $('#div1').css("background", "url(/site_media/images/t1.jpg)");
    
}).bind("mouseout", function () {
    if ($("#div1").attr("class") == 'div_3') { return; }; $("#div1").attr("class", "div_2"); $('#div1').css("background", "");
}).bind("click", function () {
    Bgmove(); $("#div1").attr("class", "div_3"); $('#div1').css("background", "url(/site_media/images/t1.jpg)"); $("#divMess2").css("display", "block");
});
$('#div2').bind("mouseover", function () {
    if ($("#div2").attr("class") == 'div_3') { return; }; $("#div2").attr("class", "div_2"); $('#div2').css("background", "url(/site_media/images/t2.jpg)");
}).bind("mouseout", function () {
    if ($("#div2").attr("class") == 'div_3') { return; }; $("#div2").attr("class", "div_2"); $('#div2').css("background", "");
}).bind("click", function () {
    Bgmove(); $("#div2").attr("class", "div_3"); $('#div2').css("background", "url(/site_media/images/t2.jpg)"); $("#divMess2").css("display", "block");
});
$('#div3').bind("mouseover", function () {
    if ($("#div3").attr("class") == 'div_3') { return; }; $("#div3").attr("class", "div_2"); $('#div3').css("background", "url(/site_media/images/t3.jpg)");
}).bind("mouseout", function () {
    if ($("#div3").attr("class") == 'div_3') { return; }; $("#div3").attr("class", "div_2"); $('#div3').css("background", "");
}).bind("click", function () {
    Bgmove(); $("#div3").attr("class", "div_3"); $('#div3').css("background", "url(/site_media/images/t3.jpg)"); $("#divMess2").css("display", "block");
});

$('#div4').bind("mouseover", function () {
    if ($("#div4").attr("class") == 'div_3') { return; }; $("#div4").attr("class", "div_2"); $('#div4').css("background", "url(/site_media/images/t4.jpg)");
}).bind("mouseout", function () {
    if ($("#div4").attr("class") == 'div_3') { return; }; $("#div4").attr("class", "div_2"); $('#div4').css("background", "");
}).bind("click", function () {
    Bgmove(); $("#div4").attr("class", "div_3"); $('#div4').css("background", "url(/site_media/images/t4.jpg)"); $("#divMess2").css("display", "block");
});
$('#div5').bind("mouseover", function () {
    if ($("#div5").attr("class") == 'div_3') { return; }; $("#div5").attr("class", "div_2"); $('#div5').css("background", "url(/site_media/images/t5.jpg)");
}).bind("mouseout", function () {
    if ($("#div5").attr("class") == 'div_3') { return; }; $("#div5").attr("class", "div_2"); $('#div5').css("background", "");
}).bind("click", function () {
    Bgmove(); $("#div5").attr("class", "div_3"); $('#div5').css("background", "url(/site_media/images/t5.jpg)"); $("#divMess2").css("display", "block");
});

function Bgmove() {
    for (var i = 1; i <= 5; i++) {
        $("#div" + i).attr("class", "div_1"); $("#div" + i).css("background", "");
    }
    window.location = "http://n2.113388.net/Register.aspx";
}

