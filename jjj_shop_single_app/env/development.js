let url = 'https://single-java.jjjshop.net';
if(process.env.NODE_ENV != 'development'){
	url = '/api';
}
export default {
	url
}
