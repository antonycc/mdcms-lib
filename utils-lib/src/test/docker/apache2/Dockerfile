FROM httpd:2.4

# Access content directly
#   http://localhost:8081/index.html
# Or browse directories:
# curl --silent  --include --request GET 'http://localhost:8081/sub/' | grep '<li>' | grep -v '<ul>'
#   <li><a href="1.txt"> 1.txt</a></li>
#   <li><a href="2.txt"> 2.txt</a></li>

# Place static HTML in Apache doc root in Docker:
COPY ./public-html/ /usr/local/apache2/htdocs/

# Cusomize Apache config with:
#   docker run --rm httpd:2.4 cat /usr/local/apache2/conf/httpd.conf > ./src/test/docker/apache2/my-httpd.conf
# COPY ./my-httpd.conf /usr/local/apache2/conf/httpd.conf
