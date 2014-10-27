$("#password").change(function () {
    $(this).val(sha1($(this).val()));
});