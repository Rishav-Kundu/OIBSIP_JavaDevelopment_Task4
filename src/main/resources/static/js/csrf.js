(function(){
  const tokenMeta = document.querySelector('meta[name="_csrf"]');
  const headerMeta = document.querySelector('meta[name="_csrf_header"]');
  const token = tokenMeta ? tokenMeta.getAttribute('content') : null;
  const header = headerMeta ? headerMeta.getAttribute('content') : 'X-CSRF-TOKEN';

  window.CSRF = { token, header };

  // Helper for fetch: adds CSRF header automatically when token is present
  window.fetchWithCsrf = function(url, options = {}){
    options.headers = options.headers || {};
    if (token) options.headers[header] = token;
    return fetch(url, options);
  };
})();
