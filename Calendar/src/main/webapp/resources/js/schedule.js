/**
 * 
 */
var scheduleService = (function() {
	function getList(param, callback, error) {
		var uno = param.uno;
		var scheduleDate = param.scheduleDate;
		$.getJSON("/board/diary/" + uno + "/" + scheduleDate + ".json",
				function(data) {
					if (callback) {
						callback(data);
					}
				}).fail(function(xhr, status, err) {
			if (error) {
				error();
			}
			;
		})
	}

	function add(schedule, callback, error) {
		$.ajax({
			type : 'post',
			url : '/board/diary/new',
			data : JSON.stringify(schedule),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		})
	}

	return {
		getList : getList,
		add : add
	};
})();