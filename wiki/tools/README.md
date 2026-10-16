# Tools of the wiki

The pages in `wiki/` are the [wiki](https://github.com/ugcs/ugcs-java-sdk/wiki) of this repository:
the guide pages are written by hand, and the reference pages - `API-*` (requests), `Objects-*`,
`Enums` and `Message-Types` - are generated from the protocol definitions of the SDK by
`generate.py` (Python 3.8 or later, standard library only). For a new SDK version, from the root of
the repository:

```sh
python3 wiki/tools/generate.py . wiki
```

It writes only the reference pages, and deletes one of them which is no longer generated; check the
diff. Where a request or an object goes is set in the script (`API_GROUPS`, `DOMAIN_SECTIONS`,
`DOMAIN_OVERRIDES`): one which fits nowhere stops it, with the name to add. `NOTES` holds the
descriptions of fields which the `.proto` files leave without a comment.

Tests, with pytest:

```sh
python3 -m pytest wiki/tools
```

To publish, copy the pages of `wiki/` - not this `tools` folder - into the wiki's repository,
`https://github.com/ugcs/ugcs-java-sdk.wiki.git`.
