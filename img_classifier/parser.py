#-------------------------------------------------------------------------------
# Instituto Tecnologico de Costa Rica-Area Academica Ingenieria en Computadores
# Principios de Sistemas Operativos - Tarea Corta 2 - Containers Jobs
# Estudiante-carnet: Giovanni Villalobos Quiros - 2013030976
#-------------------------------------------------------------------------------

def getIpList(configFile):
    f = open(configFile,"r")
    #Makes the list of not trusted and accepted
    listNotTrusted = []
    listAccepted = []
    #Reads the file always first the Accepted and then Not Trusted:
    if(f.readline() == "Accepted:\n"):
        for line in f:
            #check is the NotTrusted is reach that means end of Accepted 
            if (line == "Not Trusted:\n"):
                break
            else:
                listAccepted.append(line.splitlines()[0])
        for line in f:
            listNotTrusted.append(line.splitlines()[0])
    #Return a tuple of the lists
    return listNotTrusted, listAccepted

def parse():
        return getIpList("../carpetaDocker/configuracion.config")
