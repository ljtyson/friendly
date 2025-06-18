/**
 *
 * CustomJSPageView
 *
 * ccampos 06/24/2019
 * 
 * The current intent of this file is to allow JS to be execute with variables populated by a controller when included
 * in the "Title & Instructions" of a Data Form or Data View. In the future, it may be useful (and more consistent) to have this
 * particular file included in all Data Forms/Views to allow for the managing of JS includes to come from one source.
 **/

// Immediately hide the tabs to prevent flickering 
document.write('<style id="temp-tab-hiding">'+
    '#tabMenu > li { display: none !important; }'+
    '#tabMenu:after { content: "Loading..."; padding: 10px; font-style: italic; }'+
'</style>');

// Create a global flag to prevent duplicate rapid searches
if (typeof window.rapidSearchBuilt === 'undefined') {
    window.rapidSearchBuilt = false;
}

// Show or hide Rapid Search - Currently hooked up to the current User Role
jQuery(document).ready(function(){
    // Only build rapid search if not already built
    if (!window.rapidSearchBuilt) {
        switch ("role.administration") {
            case "role.securityRepresentative":
            case "role.hro":
            case "role.eisdIt":
            case "role.badgeOffice":
            case "role.sensitivePositions":
                break;
            default:
                U.buildRapidSearch("role.administration");
                window.rapidSearchBuilt = true;
                break;
        }
    } else {
    }
});

/**
 * Custom JavaScript utility functions
 */

var currentUserRole = "role.administration";
var applicantType = "$applicantType";
var dataObjectKey = "$dataObjectKey";
var parentId = "$parentId";
var parentIdSource = "$parentIdSource";
var applicantTypeSource = "$applicantTypeSource";
var parentIdError = "$parentIdError";
var parentIdLookupError = "$parentIdLookupError";
var sqlError = "$sqlError";

// Create a global flag to prevent duplicate tab processing
if (typeof window.tabProcessingStarted === 'undefined') {
    window.tabProcessingStarted = false;
}

// Function to hide child object tabs when parent case's applicant type is 'ADProvisioning'
jQuery(document).ready(function() {
    // Only start tab processing if not already started
    if (!window.tabProcessingStarted) {
        window.tabProcessingStarted = true;
        
        // Check if we're on a child object list view page by looking for parentId field in DOM
        var parentIdField = jQuery("input[name='parentId']");
        
        // Only proceed if we have a parentId field
        if (parentIdField.length > 0) {
            var actualParentId = parentIdField.val();
            
            // Get applicant type - either from controller or by loading it now
            if (applicantType === "$applicantType" || !applicantType) {
                
                loadApplicantTypeAndProcessTabs(actualParentId);
            } else {
                processTabs(applicantType);
            }
        } else {
            window.tabProcessingStarted = false; // Reset the flag
        }
    } else {
    }
});

/**
 * Load this same page but pass the parentId parameter to the controller
 * to get the applicant type
 */
function loadApplicantTypeAndProcessTabs(parentId) {
    
    // Create a script element to load our same script but pass the parentId
    var script = document.createElement('script');
    script.src = "page.request.do?page=cbs.utility.customjs&parentId=" + parentId;
    
    // When the script loads, it will set the applicantType and call processTabs
    script.onload = function() {
        
        // Wait a moment for the variables to be set
        setTimeout(function() {
            var loadedApplicantType = window.applicantType;
            if (loadedApplicantType && loadedApplicantType !== "$applicantType") {
                processTabs(loadedApplicantType);
            } else {
                
                // Try one more approach - create a hidden iframe and extract value
                var iframe = document.createElement('iframe');
                iframe.style.display = 'none';
                iframe.src = "workflow.do?dataObjectKey=object.case&trackingId=" + parentId;
                
                iframe.onload = function() {
                    try {
                        // Try to extract the applicant type from the case form
                        var iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
                        var applicantTypeText = iframeDoc.body.innerHTML;
                        
                        if (applicantTypeText.indexOf("applicantTypeAdOnly") !== -1) {
                            processTabs("applicantTypeAdOnly");
                        } else {
                            processTabs("NotFound");
                        }
                        
                        // Remove the iframe
                        document.body.removeChild(iframe);
                    } catch (e) {
                        processTabs("NotFound");
                        
                        // Remove the iframe
                        document.body.removeChild(iframe);
                    }
                };
                
                document.body.appendChild(iframe);
            }
        }, 500);
    };
    
    script.onerror = function() {
        processTabs("NotFound");
    };
    
    document.head.appendChild(script);
}

/**
 * Process the tabs based on the applicant type
 */
function processTabs(applicantType) {
    // Get the current object type from URL parameter
    var urlParams = new URLSearchParams(window.location.search);
    var dataObjectKey = urlParams.get("dataObjectKey") || "";
    
    // Remove the temporary CSS that was hiding tabs
    jQuery("#temp-tab-hiding").remove();
    
    // List of child objects for which we want to hide tabs
    var relevantChildObjects = [
        "object.caseContract",
        "object.recordCheck",
        "object.correspondence",
        "object.documents",
        "object.loi",
        "object.comment",
        "object.workflowHistory",
        "object.hrlobUpdate"
    ];
    
    // Check if we're on a relevant child object
    var isRelevantObject = relevantChildObjects.indexOf(dataObjectKey) !== -1;
    
    // If we don't have a dataObjectKey from URL, try to get it from the URL path
    if (!isRelevantObject) {
        var url = window.location.href;
        for (var i = 0; i < relevantChildObjects.length; i++) {
            var objType = relevantChildObjects[i].replace("object.", "");
            if (url.indexOf(objType) !== -1) {
                dataObjectKey = relevantChildObjects[i];
                isRelevantObject = true;
                break;
            }
        }
    }
    
    
    // Only proceed if we're on a relevant child object page
    if (isRelevantObject) {
        // If the applicant type is ADProvisioning, hide all child object tabs
        if (applicantType === "applicantTypeAdOnly") {
            
            	jQ("#tabMenu > li > a:contains('Audit Log')").parent().remove();   
                jQ("#tabMenu > li > a:contains('Investigation')").parent().remove(); 
                jQ("#tabMenu > li > a:contains('Adjudication')").parent().remove(); 
                jQ("#tabMenu > li > a:contains('PDSSQ')").parent().remove(); 
                jQ("#tabMenu > li > a:contains('Applicant Intake')").parent().remove(); 
                jQ("#tabMenu > li > a:contains('Pre-Screening')").parent().remove(); 
                
                
        } else {
            // Show all tabs
            jQuery("#tabMenu > li").show();
        }
    } else {
        // Show all tabs since we're not on a relevant child object
        jQuery("#tabMenu > li").show();
    }
}
