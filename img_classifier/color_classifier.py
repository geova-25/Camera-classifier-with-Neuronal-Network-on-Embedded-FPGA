#-------------------------------------------------------------------------------
# Instituto Tecnologico de Costa Rica-Area Academica Ingenieria en Computadores
# Principios de Sistemas Operativos - Tarea Corta 2 - Containers Jobs
# Estudiante-carnet: Giovanni Villalobos Quiros - 2013030976
# Based on code from - Basado en codigo tomado de
# https://github.com/fengsp/color-thief-py
#-------------------------------------------------------------------------------

from colorthief import ColorThief

def determine_predominant_color(name):
    color_thief = ColorThief(name)
    # get the dominant color 1 for the quickest but lower accuracy
    dominant_color_tuple = color_thief.get_color(quality=1)
    #Obtains the maximun value of the tuple
    dominant_color_value = max(dominant_color_tuple)
    #Obtains the index of it
    dominant_color_index = dominant_color_tuple.index(dominant_color_value)
    #Index zero means Red, 1 means green and 2 means Blue and return the first lettter capital
    #to concatenate it to the string of path
    if(dominant_color_index == 0):
        dominant_color_name = "R"
    elif(dominant_color_index == 1):
        dominant_color_name = "G"
    else:
        dominant_color_name = "B"
    return dominant_color_name
