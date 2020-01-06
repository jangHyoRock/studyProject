//Component 생성 전 실행 로직 추가

// API Error 관련 세션 값 제거
for (var i = 0; i < sessionStorage.length; i++) {
	if (sessionStorage.key(i).indexOf('apiErr_') > -1) {
		sessionStorage.removeItem(sessionStorage.key(i));
	}
}