from PIL import Image
import io
import rawpy
import imageio

raw = rawpy.imread('1.nef')
rgb = raw.postprocess()
imageio.imsave('default.tiff', rgb)