var renderContext = require("webgl-video-renderer").setupCanvas(document.getElementById("camera"));
var wcjs_gs_addon = new require("wcjs-gs");
var wcjs_gs = new wcjs_gs_addon.JsPlayer();
wcjs_gs.parseLaunch("videotestsrc ! appsink name=sink");
wcjs_gs.addAppSinkCallback("sink",
	function(type, frame) {
		if(type == wcjs_gs.AppSinkNewSample) {
			renderContext.render(frame, frame.width, frame.height, frame.planes[1], frame.planes[2]);
		}
	} );
wcjs_gs.setState(wcjs_gs.GST_STATE_PLAYING);