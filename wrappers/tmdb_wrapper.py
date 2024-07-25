import tmdbsimple as tmdb
import shutil
import requests
from time import sleep

lista = ['2001 uma odisseia', 'interstellar', 'shin kamen rider',
         'tarzan', 'die hard', 'taken']

def gravarImagens(id, link):
    resposta = requests.get(url=link, stream=True)
    diretorio = f'./img/{id}.jpg'
    if resposta.status_code == 200:
        with open(diretorio, 'wb') as f:
            f.write(resposta.content)
    
    return diretorio


def gravarCategoriaMidias(idCategoria, idMidia):
    f = open('./wrappers/insert_categorias_midias.txt', 'a')
    f.write(f'INSERT into midia_categoria (id_midia, id_categoria) values ({idMidia}, {idCategoria});\n')
    f.close()
    
def gravarMidias(info):
    f = open('./wrappers/inserts.txt', 'a')
    f.write(f"""INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '{info['id_midia']}',
        '{info['nome']}',
        {info['filme']},
        '{info['data_lancamento']}',
        '{info['img']}',
        {info['adulto']},
        '{info['sinopse']}',
        {info['qtd_copias']});\n""")

    f.close()
       
def pegarId():
    for item in lista:
        search = tmdb.Search()
        search.movie(query=item)
        resul = search.results[0]
        
        movie = tmdb.Movies(resul['id']).info(language='pt-BR')
        print(movie)
                
        info = {
            "id_midia": movie['id'],
            "nome": movie['title'],
            "filme": 'TRUE',
            "data_lancamento": movie['release_date'],
            "img": gravarImagens(movie['id'], f"https://image.tmdb.org/t/p/original/{movie['poster_path']}"),
            "adulto": str(movie['adult']).upper(),
            "sinopse": movie['overview'],
            "qtd_copias": 10,
            "categorias": movie['genres']
        }
        
        gravarMidias(info)
        for categoria in info['categorias']:
            gravarCategoriaMidias(categoria['id'], info['id_midia'])
            
        sleep(5)
        

    
            




tmdb.API_KEY = "8e70e2a94ddafd4d68ce3a6450319e8e"

#serie = tmdb.TV(94997)


pegarId()

