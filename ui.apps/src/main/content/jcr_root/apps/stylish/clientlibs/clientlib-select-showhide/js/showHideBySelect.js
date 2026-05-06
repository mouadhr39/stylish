(function(document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", (e) => {

        showHide();

         $('.card-type').on('change', (e) => {
                showHide();
            })
    });



    const showHide = () => {
      $('.show-hide-container').each((idx, element) => {
                if ($(element).find('.card-type').val() === 'single-image-type') {
                    $(element).find('.second-card-group').hide();
                } else {
                    $(element).find('.second-card-group').show();
                }
            });
    }
})(document,Granite.$);
