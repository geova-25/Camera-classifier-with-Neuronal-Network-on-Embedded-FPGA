#-------------------------------------------------------------------------------
# Instituto Tecnologico de Costa Rica-Area Academica Ingenieria en Computadores
# Introduccion al Reconocimiento de Patrones - Tarea Corta 2 - Containers Jobs
# Estudiante-carnet: Giovanni Villalobos Quiros - 2013030976
# Based on code from - Basado en codigo tomado de
# https://stackoverflow.com/questions/42458475/sending-image-over-sockets-only-in-python-image-can-not-be-open
#-------------------------------------------------------------------------------

import random
import socket, select
from random import randint
import os
from PIL import Image

#------------------------------------------------------------------------------
#----Some local variables

folderNameOriginal = "../received_Image"
folderNameNew = ""
imgcounter = 1
folderCounter = 0;
imgExtension = ".data"
connected_clients_sockets = []
server_socket = ""
notTrustedFolder = "/Not_Trusted"
HOST = "0.0.0.0"
PORT = 6666
MAX_CONS = 50
buffer_size = 51200
dataFinal = ''
sizeActual = 0

#------------------------------------------------------------------------------
#----Get the data from the config file using the parser.py file imported

#notTrustedList, acceptedList  = parserConfig.parse()

#------------------------------------------------------------------------------

#------------------------------------------------------------------------------

#------------------------------------------------------------------------------
#----This function is in charge of the connection to the socket of the server

def connectSocket():
    #global variabes for sockets
    global connected_clients_sockets, server_socket, maxCons
    #Uses the function of socket to start it
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    #The server binds to the Host and Port defined above
    server_socket.bind((HOST, PORT))
    #The socket listen to maximum number of conection defined above in global
    server_socket.listen(MAX_CONS)
    #A list of the connected sockets available to make ir more accesible
    connected_clients_sockets.append(server_socket)
    print("Using port: " + str(PORT))


#------------------------------------------------------------------------------

#------------------------------------------------------------------------------
#----This function is in charge of the receive image through socket to the server

def reciveImage(data, ip):
    global folderNameNew, imgExtension, imgcounter
    #The original path of where the image will be received and adds the image
    #counter and the extension of the image that was received from the client
    tempImgPath = "Images/"+str(imgcounter) + imgExtension
    #Create a file using the path defined above
    myfile = open(tempImgPath, 'wb')
    #Write the data of the received image because the file color_classifier needs
    #the image to be stored
    myfile.write(data)
    #Close the file
    myfile.close()
    print("Store succesfull")
    #im = Image.frombytes("L", (176,144), dataFinal)
    im = Image.frombytes("RGBA", (195,260), dataFinal)
    print("Creo")
    im.save("Images/"+str(imgcounter) + ".jpg", "JPEG")
    print("Guardo")
    imgcounter += 1
#------------------------------------------------------------------------------
#----This function is in charge of the receive data through socket


def receiveFromSocket(sock):
    global imgExtension, buffer_size, dataFinal, sizeActual
    #If there are available data and not error occured
    try:
        size_str = ""
        size_int = 0
        #Obtains data from buffer of socket
        print("--------------")
        sizea = str(sock.recv(buffer_size)).replace('b','').replace("'","")
        print("Size Bytes: ", sizea)
        size_str = str(sizea)
        print("Size Srt: ", size_str)
        size_int = int(size_str)
        print("Size Int: ", size_int)
        dataFinal = sock.recv(buffer_size)
        print("len data final: ", len(dataFinal))
        while(size_int != len(dataFinal)):
            print("Waiting for data")
            dataFinal = dataFinal + sock.recv(buffer_size);
            print("len data final: ", len(dataFinal))
            #Call function received image
        reciveImage(dataFinal, sock.getpeername()[0]);

        print("Image Processed")

    except:
        #In error close the socket or call it after Bye to destroy it
        print("Socket close")
        sock.close()
        #Eliminated from the list of clients sockets
        connected_clients_sockets.remove(sock)

connectSocket()
#showNetworkInfo()

#Check always incomming sockets connections
while True:
    #Makes the waitable objects sockets return in a list to know which of them are ready
    #to be read, write or are error or exeption at the moment, in this case
    #only ready to read
    read_sockets, write_sockets, error_sockets = select.select(connected_clients_sockets, [], [])

    for sock in read_sockets:
        #If the socket is the server means a new connection is ready
        if sock == server_socket:
            #First it is accepted and then rejected or not, this because
            #is need to be connected to reach the ip info
            sockfd, client_address = server_socket.accept()
            #If it is in the list of accepted or not trusted keeps the connection
            if (1==1):
                #sockfd.send("Accepted")
                connected_clients_sockets.append(sockfd)
                print ("Socket initializing with host %s" %  sockfd.getpeername()[0])
            #If not in any list then it is rejected
            else:
                #sockfd.send("Not Accepted")
                sockfd.close()
                print("Ip not accepted")
        #If it is other socket it means incomming info
        else:
            receiveFromSocket(sock)
#close server at the end
server_socket.close()
