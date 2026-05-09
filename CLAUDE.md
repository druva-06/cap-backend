## Documentation Rule

**Update the relevant file in `docs/` after every change. Never skip documentation to finish faster.**

| Change | File |
|---|---|
| New or modified REST endpoint | `docs/api/API_REFERENCE.md` |
| Database schema change (table / column / index) | `docs/DATABASE_SCHEMA.md` |
| New service, repository, or architectural pattern | `docs/ARCHITECTURE.md` |
| New environment variable or Spring profile change | `docs/ENVIRONMENTS.md` |
| Dev-mode behaviour or local setup change | `docs/DEVELOPMENT_GUIDE.md` |

**Every new endpoint in `API_REFERENCE.md` must include all of these sections** (template is at the top of that file):
- `Description` — one-line summary
- `Auth` + `Roles` — who can call it
- `Headers` — Authorization, Content-Type, any custom headers
- `Path Parameters` / `Query Parameters` / `Request Body` — full input spec
- `Tables Affected` — which DB tables are read or written, and what is stored
- `Flow` — step-by-step: filter → controller → service → repository → transformer → response
- `Response` — success JSON example
- `Error Responses` — status codes and reasons

Rules:
- **Never write documentation directly in the main agent.** After all code changes are complete, spawn a `general-purpose` sub-agent via the `Agent` tool to write/update the doc files.
- The sub-agent prompt must include: the endpoint added/changed, the files modified, which doc file to update, and the template from the top of `docs/api/API_REFERENCE.md`.
- Run the sub-agent in the background (`run_in_background: true`) so the main agent is not blocked.
- Create the doc file if it does not exist.

---

## graphify

This project has a graphify knowledge graph at graphify-out/.

Rules:
- Before answering architecture or codebase questions, read graphify-out/GRAPH_REPORT.md for god nodes and community structure
- If graphify-out/wiki/index.md exists, navigate it instead of reading raw files
- For cross-module "how does X relate to Y" questions, prefer `graphify query "<question>"`, `graphify path "<A>" "<B>"`, or `graphify explain "<concept>"` over grep — these traverse the graph's EXTRACTED + INFERRED edges instead of scanning files
- After modifying code files in this session, run `graphify update .` to keep the graph current (AST-only, no API cost)
