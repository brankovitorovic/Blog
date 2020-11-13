function getCookie(){
	return sessionStorage.getItem("Cookie");
}

function set_cookie(value) {
    sessionStorage.setItem("Cookie",value);
}
function delete_cookie() {
    sessionStorage.removeItem("Cookie");
}
