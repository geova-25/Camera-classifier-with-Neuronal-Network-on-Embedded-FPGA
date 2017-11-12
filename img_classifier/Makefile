all: img

show:
				echo "----------------------docker ps"
				sudo docker ps
				echo "----------------------docker ps -a"
				sudo docker ps -a
				echo "----------------------docker images"
				sudo docker images

img:
				sudo docker build -t img_server_classifier .

stop:
				sudo docker stop $(sudo docker ps -a -q)
rmc:
			  VAR = sudo docker ps -a -q
				sudo docker rm VAR
im:
				sudo docker rmi $(sudo docker images | grep "<none>")
				sudo docker rmi img_server_classifier
				sudo docker build -t img_server_classifier .
