# encrypted-files-transfer - Proyecto final del curso de Seguridad, universidad Icesi

Este programa permite enviar un archivo entre dos computadores, de manera cifrada. Se emplea para el cifrado el algoritmo AES, con clave de 128 bits. La clave a emplear es una clave de sesión, generada haciendo uso del algoritmo Diffie-Hellman. El programa emisor y el receptor calcularán un hash MD5 del archivo no cifrado, para garantizar la integridad del proceso.
