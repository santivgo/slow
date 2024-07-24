import tmdbsimple as tmdb

tmdb.API_KEY = "8e70e2a94ddafd4d68ce3a6450319e8e"

movie = tmdb.Movies(94997)
serie = tmdb.TV(94997)
print(movie.info())
print(serie.info())
