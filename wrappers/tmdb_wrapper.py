import tmdbsimple as tmdb
import shutil
import requests
from time import sleep
import mysql.connector
from mysql.connector import Error

lista = ['2001 uma odisseia', 'interstellar', 'shin kamen rider',
         'tarzan', 'die hard', 'taken', 'Fifty Shades of Grey']
def lerBlob(diretorio):
    with open(diretorio, 'rb') as file:
        binarydata =  file.read()
    return binarydata
    
def gravarImagens(id, link):
    resposta = requests.get(url=link, stream=True)
    diretorio = f'./img/{id}.jpg'
    if resposta.status_code == 200:
        with open(diretorio, 'wb') as f:
            f.write(resposta.content)
    
    return diretorio


def gravarCategoriaMidias(idCategoria, idMidia):
    host="localhost"
    usr = "root"
    pwd = '99586090'
    db = 'slowdb'
    
    try:
        connection = mysql.connector.connect(host=host,user=usr, password=pwd, database=db)
        cursor = connection.cursor()
        insert = 'INSERT into midia_categoria (id_midia, id_categoria) values (%s, %s);'
        values = (idMidia, idCategoria)
        cursor.execute(insert, values)
        connection.commit()

    except:
        pass
 
    
def gravarMidias(info):
    host="localhost"
    usr = "root"
    pwd = '99586090'
    db = 'slowdb'
    
    try:
        connection = mysql.connector.connect(host=host,user=usr, password=pwd, database=db)
        cursor = connection.cursor()

        insert = f"""INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (%s, %s, %s, %s, %s, %s, %s, %s);"""
        
        
        values = (info['id_midia'],
        info['nome'],
        info['filme'],
        info['data_lancamento'],
        lerBlob(info['img']),
        info['adulto'],
        info['sinopse'],
        info['qtd_copias'])
        cursor.execute(insert, values)
        connection.commit()
    except Error as e:
        print(e)
        pass

       
def pegarId():
    for item in lista:
        search = tmdb.Search()
        search.movie(query=item)
        resul = search.results[0]
        
        movie = tmdb.Movies(resul['id']).info(language='pt-BR')
        
                
        info = {
            "id_midia": movie['id'],
            "nome": movie['title'],
            "filme": True,
            "data_lancamento": movie['release_date'],
            "img": gravarImagens(movie['id'], f"https://image.tmdb.org/t/p/original/{movie['poster_path']}"),
            "adulto": movie['adult'],
            "sinopse": movie['overview'],
            "qtd_copias": 10,
            "categorias": movie['genres']
        }
        
        gravarMidias(info)
        for categoria in info['categorias']:
            gravarCategoriaMidias(categoria['id'], info['id_midia'])
            
        sleep(5)
        

    
            



tmdb.API_KEY = "8e70e2a94ddafd4d68ce3a6450319e8e"


pegarId()

