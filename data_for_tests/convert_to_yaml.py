#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import json
from pathlib import Path

# Укажи свои пути здесь один раз
SRC = Path(r"C:\Users\yarriiik\PycharmProjects\HackVTB\data_for_tests\openapi.json")
DST = Path(r"C:\Users\yarriiik\PycharmProjects\HackVTB\data_for_tests\openapi.yml")

try:
    import yaml
except ImportError:
    raise SystemExit("PyYAML не установлен. Установи: pip install pyyaml")

def json_to_yaml(src_path: Path, dst_path: Path) -> None:
    if not src_path.exists():
        raise FileNotFoundError(f"Input file not found: {src_path}")

    with src_path.open("r", encoding="utf-8") as f:
        data = json.load(f)

    text = yaml.safe_dump(
        data,
        sort_keys=False,     # сохранить порядок ключей
        allow_unicode=True   # корректно писать кириллицу
    )

    # на всякий случай принудительно .yml
    dst_path = dst_path.with_suffix(".yml")

    dst_path.parent.mkdir(parents=True, exist_ok=True)
    with dst_path.open("w", encoding="utf-8", newline="\n") as f:
        f.write(text)

if __name__ == "__main__":
    json_to_yaml(SRC, DST)
    print(f"Converted: {SRC} -> {DST}")
