Usa el agente `issue-manager` para crear un issue de tipo TODO (tarea para despues) en el repo.

## Instrucciones

El usuario te da una idea rapida de algo que quiere hacer despues. Tu trabajo:

1. **Interpretar** la idea del usuario — puede ser una frase corta o informal
2. **Decidir el tipo y labels** correctos segun el contenido (type:code, type:design, type:asset, etc.)
3. **Agregar label `priority:low`** por defecto (es algo para despues, no urgente)
4. **Invocar el agente `issue-manager`** con una descripcion clara para que cree el issue con formato estandar
5. **Reportar** el issue creado con su numero y link

## Reglas

- Si el usuario no especifica mod, intenta inferirlo del contexto. Si no es claro, pregunta.
- Si la idea es vaga, haz tu mejor esfuerzo para redactar una descripcion clara pero NO inventes requisitos que el usuario no menciono.
- Siempre incluir `priority:low` a menos que el usuario diga explicitamente que es urgente/importante.
- El titulo debe ser corto y descriptivo (<70 chars).

## Idea del usuario

$ARGUMENTS
