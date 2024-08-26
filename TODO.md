## TODO

## Data
Encrypt all db files besides sensitive entries. Best third party software?
Now DB opens by entry, is this more secure? Damage performace? Global access in the app or only in certain contexts?

## UI/UX
Everything to improve

## Logs
Improve logs tags/categories, clean some and add informatives and exeptions and erros

## Error Handling/validation
Lacks of try-catchs, specially when using indexes, and add more validation of users input data

## Compose/ navigator/ scopes/ context / access 
Consider ModelView implementation. Recheck hierarchy of scopes of different parts of application... More restricted and specific tends to be better.


## Finish Http requests/response and Credit card encryption/decryption
Getting biometrically the secure keys is nice, but implies dificult treatment of data structures encryprion because cant be used many times the same key for different related parameters without re authentication. One possible solution is to sum all of them  in one block to encrypt, split arbitrarily to save, rejoin to decrypt and split again to use. Sensible data is mostly fixed lengthed. 

