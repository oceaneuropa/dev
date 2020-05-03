// --------------------------------------------------------
// Full screen
// --------------------------------------------------------
var isFullScreen = false;

function toggleFullScreen() {
	if (document.fullscreenElement == null) {
		if (isFullScreen) {
			openFullscreen();
			closeFullscreen();
		} else {
			openFullscreen();
		}
		isFullScreen = true;

	} else {
		closeFullscreen();
		isFullScreen = false;
	}
}

function openFullscreen() {
	if (document.documentElement.requestFullscreen) {
		document.documentElement.requestFullscreen();
	} else if (document.documentElement.mozRequestFullScreen) {
		/* Firefox */
		document.documentElement.mozRequestFullScreen();
	} else if (document.documentElement.webkitRequestFullscreen) {
		/* Chrome, Safari & Opera */
		document.documentElement.webkitRequestFullscreen();
	} else if (document.documentElement.msRequestFullscreen) {
		/* IE/Edge */
		document.documentElement.msRequestFullscreen();
	}
}

function closeFullscreen() {
	if (document.exitFullscreen) {
		document.exitFullscreen();
	} else if (document.mozCancelFullScreen) {
		document.mozCancelFullScreen();
	} else if (document.webkitExitFullscreen) {
		document.webkitExitFullscreen();
	} else if (document.msExitFullscreen) {
		document.msExitFullscreen();
	}
}
