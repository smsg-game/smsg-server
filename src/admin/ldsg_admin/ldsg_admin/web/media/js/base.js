/* base on jquery.js and jquery-utils.js */
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, ""); 
}

String.prototype.safe = function() {
    // safe string for html
    return this.replace(/\"/g, "&quot;").replace(/\'/g, "&#39;").replace(/>/g, "&gt;").replace(/</g, "&lt;");
}

$(document).ready(function() {
    $.ajaxSetup({cache: false}); // fixed ie ajax cache problem.
});