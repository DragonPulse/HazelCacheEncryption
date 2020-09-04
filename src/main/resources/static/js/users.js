$(document).ready( function () {

	 var table = $('#usersTable').DataTable({
			"sAjaxSource": "/users/",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "accountId"},
		        { "mData": "username" },
				{ "mData": "email" },
				{ "mData": "firstName" },
				{ "mData": "lastName" },
				{ "mData": "about" },
				{
                   "mData": null,
                    "bSortable": false,
                    "mRender": function(data, type, full) {
                        return '<button>Add to Cache!</button>';
                  }
                  }
			]
	 })


	 var cacheInfoTable = $('#cacheInfoTable').DataTable({
     			"sAjaxSource": "/users/cached",
     			"sAjaxDataProp": "",
     			"order": [[ 0, "asc" ]],
     			"aoColumns": [
     			    { "mData": "key"},
     		        { "mData": "value" },
     		        {
                      "mData": null,
                      "bSortable": false,
                      "mRender": function(data, type, full) {
                          return '<button>Decrypt!</button>';
                       }
                     }
     			]
     	 })

     $('#usersTable tbody').on( 'click', 'button', function () {
        var data = table.row( $(this).parents('tr') ).data();
         $.get( "/users/"+data.accountId, function( data ) {

           $('#cacheInfoTable').DataTable().ajax.reload();



         });
      } );

      $('#cacheInfoTable tbody').on( 'click', 'button', function () {
              var data = cacheInfoTable.row( $(this).parents('tr') ).data();
               $.get( "/users/decrypt/"+data.value, function( data ) {
                 alert(data);
               });
            } );

});

