// $(document).on( 'click', '#banner a.beer-link', function( evt ) {
// 	evt.preventDefault();
// 	$(content).load( evt.target.href );
// 	$(evt.target).addClass( "selected" );
// 	$(evt.target).siblings("li").removeClass( "selected" );
// });

$(window).load( function( ) {

	var twentyOne = $.cookie( "twentyOne" );

	if ( ! twentyOne ) { 
		var options = {'backdrop': 'static'};
		$('#myModal').modal(options);

		$('#myModal').on('hide', function() {
			$.cookie( "twentyOne", true );
		});
	}

});

