/**
* Main JS file for site-wide implementation
*/
var cls;
var doGrid;
var doUIGrid;
var jQ = jQuery;
$(function() {
    if (!window.console) {
        console = {
            log: function() {}
        };
    }
    $("#sideList").find("a").each(function() {
        if ($.trim($(this).text()).indexOf('New Field Investigation Leads') > -1) {
            $(this).text("Add Field Lead");
        }
    });
    $("#sideList").find("a").each(function() {
        if ($.trim($(this).text()).indexOf('New CVS') > -1) {
            $(this).text("New CVS Report");
        }
    });
    if ((window.location.search.indexOf("object.fieldInvestigation") > -1 && window.location.pathname.indexOf("tracking.update.request.do") > -1) || (window.location.pathname.indexOf("tracking.update.submit.do") > -1)) {
        $("body").css("min-width", "1500px");
        $("#mainContent").children("table").css("width", "100%");
    }
    if (window.location.pathname.indexOf('/tracking.') >= 0 && window.location.pathname.indexOf('/tracking.assignment.') == -1) {
        if ($('#saveButton').length == 0) {
            $('#trackingForm input[type=text]').each(function() {
                var label = $('<label />');
                label.html($(this).val());
                $(this).replaceWith(label);
            });
            $('#trackingForm textarea').each(function() {
                var label = $('<label />');
                label.html($(this).val());
                $(this).replaceWith(label);
            });
            $('#trackingForm img[title="Select Date"]').each(function() {
                $(this).parent().next('td').html('');
                $(this).parent().html('');
            });
            $('#trackingForm select').prop("disabled", "disabled");
            $('#trackingForm input').prop("disabled", "disabled");
            $('#trackingForm select').each(function() {
                var label = $('<label />');
                label.html($(this).find('option:selected').text());
                $(this).replaceWith(label);
            });
            $('img[src*="required.gif"]').hide();
            $('input[type=radio]:checked').each(function() {
                if ($(this).attr('id').indexOf('_yes') >= 0) {
                    var label = $('<label>Yes</label>');
                    $('input[name="' + $(this).attr('name') + '"][id*="_no"]').parent().hide();
                    $(this).parent().html(label);
                } else {
                    var label = $('<label>No</label>');
                    $('input[name="' + $(this).attr('name') + '"][id*="_yes"]').parent().hide();
                    $(this).parent().html(label);
                }
            });
        }
    }
    var oldProp = $.fn.prop;
    $.fn.extend({
        prop: function(name, value) {
            if ($(this)) {
                return oldProp.apply(this, arguments);
            }
        }
    });
    $("#sideList").find("a").each(function() {
        if ($.trim($(this).text()).indexOf('New CI') > -1) {
            if ($.trim($(this).text()) === "New CI") {
                $(this).hide();
            }
        }
    });
    $("[name='dataPermissionsForm']").each(function() {
        $(this).find("tr").each(function() {
            $(this).hover(function() {
                $(this).addClass('rowHighlight');
            }, function() {
                $(this).removeClass('rowHighlight');
            });
        });
    });
    $(document).ajaxStop(function() {
        window.setTimeout(function() {
            lockThead();
        }, 1000);
    });
    $(document).ajaxStart(function() {});
    $(window).resize(function() {
        if (doGrid) {
            $('.grid').floatThead('reflow');
        }
        if (doUIGrid) {
            $('.ui-grid').floatThead('reflow');
        }
    });
});

function makeOverlay() {
    document.getElementById("mainContent").style.visibility = "hidden";
    var overlay = $('<div id="ajaxOverlayDiv"/>');
    overlay.css('position', 'fixed').css('top', '0').css('left', '0').css('width', '100%').css('height', '100%');
    var loading = $('<div></div>');
    loading.css('position', 'absolute').css('top', '35%').css('left', '45%').css('color', '#000').css('padding', '10px');
    loading.html('<img border="0" height="120px" title="Loading ..." alt="Loading ..." src="web-pub/images/logo/usaid-logo.jpg"> <br /><span id="elipseWaiting" style="font-size: 3em;">Loading .</span>');
    loading.appendTo(overlay);
    var x = 0 setInterval(function() {
        if (x % 2) {
            $('#elipseWaiting').html('Loading .');
        } else {
            $('#elipseWaiting').html('Loading . .');
            setTimeout(function() {
                $('#elipseWaiting').html('Loading . . .');
            }, 300);
        }
        x++;
    }, 600);
    overlay.appendTo(document.body);
}

function lockThead() {
    var browser = getBrowserInfo();
    if ((browser.name == "IE" || browser.name == "MSIE") && (browser.version == "8" || browser.version == "9")) {
        console.log("USING IE8 OR IE9 - TABLE HEADERS WILL NOT FLOAT");
    } else {
        if ($('.grid').length > 0 && window.location.pathname.indexOf("admin.role.datapermissions.update.request.do") == -1 && window.location.search.indexOf("dataObjectKey=object.documents") == -1 && window.location.search.indexOf("report.home.do") == -1 && window.location.search.indexOf("dataObjectKey=object.messages") == -1) {
            doGrid = true;
            $(".grid").each(function() {
                $(this).floatThead({
                    position: "absolute",
                    autoReflow: true
                });
            });
        }
        if ($('.ui-grid').length > 0 && window.location.pathname.indexOf("home.do") == -1 && window.location.search.indexOf("dataObjectKey=object.investigativeLeads") == -1 && window.location.search.indexOf("dataObjectKey=object.accessclearancesci") == -1) {
            doUIGrid = true;
            $(".ui-grid").each(function() {
                $(this).floatThead({
                    position: "absolute",
                    autoReflow: true
                });
            });
        }
    }
}

function getBrowserInfo() {
    var ua = navigator.userAgent;
    var b = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    var temp;
    if (/trident/i.test(b[1])) {
        temp = /\brv[ :]+(\d+)/g.exec(ua) || [];
        return {
            name: 'IE',
            version: (temp[1] || '')
        };
    }
    if (b[1] === 'Chrome') {
        temp = ua.match(/\bOPR\/(\d+)/) if (temp != null) {
            return {
                name: 'Opera',
                version: temp[1]
            };
        }
    }
    b = b[2] ? [b[1], b[2]] : [navigator.appName, navigator.appVersion, '-?'];
    if ((temp = ua.match(/version\/(\d+)/i)) != null) {
        b.splice(1, 1, temp[1]);
    }
    return {
        name: b[0],
        version: b[1]
    };
}
