#!/usr/bin/env python3
"""
project_tree_html.py — generate an interactive HTML directory tree with regex search.
Directories are listed before files; root is at the top.
"""

import argparse
import fnmatch
import json
import re
import sys
import webbrowser
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple

# ----------------------------------------------------------------------
DEFAULT_EXCLUDES: Set[str] = {
    ".git", ".svn", ".hg",
    "__pycache__", ".pytest_cache", ".mypy_cache", ".ruff_cache", ".tox",
    "node_modules", ".next", ".nuxt", ".parcel-cache",
    "venv", ".venv", "env",
    "dist", "build", "target",
    ".idea", ".vscode",
    ".DS_Store", "Thumbs.db",
    "*.egg-info",
}


def should_exclude(name: str, patterns: Set[str]) -> bool:
    return any(fnmatch.fnmatch(name, pat) for pat in patterns)


def walk_directory(
    root: Path,
    patterns: Set[str],
    show_hidden: bool,
    dirs_only: bool,
    max_depth: Optional[int],
) -> Dict:
    """
    Build a nested dict tree.  Children are sorted:
        directories first (alphabetical), then files (alphabetical).
    """
    tree = {
        "name": root.name or root.parts[-1] if root.parts else "",
        "type": "dir",
        "path": "",
        "children": [],
    }

    def _walk(node: Dict, current_path: Path, depth: int):
        if max_depth is not None and depth > max_depth:
            return
        try:
            entries = list(current_path.iterdir())
        except (PermissionError, OSError):
            return

        filtered = []
        for entry in entries:
            name = entry.name
            if should_exclude(name, patterns):
                continue
            if not show_hidden and name.startswith("."):
                continue
            if dirs_only and not entry.is_dir():
                continue
            filtered.append(entry)

        # Sort: directories first (False < True), then by name (case‑insensitive)
        filtered.sort(key=lambda e: (not e.is_dir(), e.name.lower()))

        for entry in filtered:
            rel_path = str(entry.relative_to(root))
            child = {
                "name": entry.name,
                "type": "dir" if entry.is_dir() else "file",
                "path": rel_path,
                "children": [],
            }
            if entry.is_dir():
                _walk(child, entry, depth + 1)
            node["children"].append(child)

    _walk(tree, root, 1)
    return tree


def generate_html(tree: Dict, root_path: Path) -> str:
    tree_json = json.dumps(tree)

    html = f'''<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Directory Tree: {root_path.name}</title>
    <style>
        * {{ box-sizing: border-box; margin: 0; padding: 0; }}
        body {{
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f8f9fa;
            padding: 20px;
            color: #1e1e1e;
        }}
        .container {{
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 20px 30px;
        }}
        h1 {{
            font-size: 1.8rem;
            margin-bottom: 0.2rem;
        }}
        .subtitle {{
            color: #6c757d;
            margin-bottom: 1rem;
            font-size: 0.95rem;
            word-break: break-all;
        }}
        .toolbar {{
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #dee2e6;
        }}
        .toolbar input[type="text"] {{
            flex: 1 1 300px;
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            font-size: 1rem;
        }}
        .toolbar input[type="text"]:focus {{
            border-color: #80bdff;
            outline: 0;
            box-shadow: 0 0 0 0.2rem rgba(0,123,255,0.25);
        }}
        .toolbar button {{
            padding: 8px 16px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 0.9rem;
            cursor: pointer;
            transition: background 0.15s;
        }}
        .toolbar button:hover {{
            background: #0069d9;
        }}
        .toolbar .stats {{
            margin-left: auto;
            font-size: 0.9rem;
            color: #6c757d;
        }}
        #results {{
            margin-bottom: 16px;
            padding: 12px 16px;
            background: #e9ecef;
            border-radius: 4px;
            display: none;
            max-height: 200px;
            overflow-y: auto;
            font-size: 0.9rem;
        }}
        #results.show {{
            display: block;
        }}
        #results .match-item {{
            padding: 4px 0;
            border-bottom: 1px solid #dee2e6;
        }}
        #results .match-item:last-child {{
            border-bottom: none;
        }}
        .tree {{ font-family: 'Consolas', 'Courier New', monospace; }}
        .tree ul {{
            list-style: none;
            padding-left: 24px;
            border-left: 1px dotted #dee2e6;
            margin-left: 8px;
        }}
        .tree li {{
            position: relative;
            padding: 2px 0 2px 8px;
        }}
        .tree li::before {{
            content: "";
            position: absolute;
            left: -20px;
            top: 0;
            bottom: 0;
            width: 20px;
            border-bottom: 1px dotted #dee2e6;
        }}
        .tree li:last-child::before {{
            border-bottom: none;
        }}
        .tree .node {{
            display: inline-block;
            padding: 2px 4px;
            border-radius: 3px;
            cursor: default;
        }}
        .tree .node:hover {{
            background: #f1f3f5;
        }}
        .tree .dir > .node {{
            cursor: pointer;
            font-weight: 500;
        }}
        .tree .dir > .node .toggle {{
            display: inline-block;
            width: 16px;
            text-align: center;
            font-weight: bold;
            color: #495057;
        }}
        .tree .dir.collapsed > ul {{
            display: none;
        }}
        .tree .file .node {{
            color: #2c3e50;
        }}
        .tree .highlight > .node {{
            background: #fff3cd;
            border: 1px solid #ffc107;
            padding: 1px 3px;
        }}
        .tree .match-path {{
            margin-left: 12px;
            color: #6c757d;
            font-size: 0.8rem;
        }}
        .tree .children-empty {{
            color: #adb5bd;
            font-style: italic;
            margin-left: 12px;
        }}
        ul, li {{ display: block; }}
    </style>
</head>
<body>
    <div class="container">
        <h1>📁 {root_path.name}</h1>
        <div class="subtitle">{root_path.resolve()}</div>

        <div class="toolbar">
            <input type="text" id="searchInput" placeholder="Regex search (e.g. test.*.py$)" title="Enter a regular expression to filter file/folder names">
            <button id="expandAllBtn">Expand All</button>
            <button id="collapseAllBtn">Collapse All</button>
            <span class="stats" id="stats"></span>
        </div>

        <div id="results"></div>

        <div class="tree" id="treeContainer"></div>
    </div>

    <script>
        const treeData = {tree_json};

        function createNode(data) {{
            const li = document.createElement('li');
            li.dataset.path = data.path;
            li.dataset.type = data.type;
            li.dataset.name = data.name;

            const nodeSpan = document.createElement('span');
            nodeSpan.className = 'node';

            if (data.type === 'dir') {{
                li.classList.add('dir');
                const toggle = document.createElement('span');
                toggle.className = 'toggle';
                toggle.textContent = '▾';
                nodeSpan.appendChild(toggle);
                nodeSpan.append(' ' + data.name + '/');

                nodeSpan.addEventListener('click', function(e) {{
                    e.stopPropagation();
                    li.classList.toggle('collapsed');
                    const isCollapsed = li.classList.contains('collapsed');
                    toggle.textContent = isCollapsed ? '▸' : '▾';
                }});

                // Append folder name BEFORE its children
                li.appendChild(nodeSpan);

                const ul = document.createElement('ul');
                if (data.children && data.children.length > 0) {{
                    // Keep the original order (directories first, then files)
                    for (let i = 0; i < data.children.length; i++) {{
                        ul.appendChild(createNode(data.children[i]));
                    }}
                }} else {{
                    const empty = document.createElement('span');
                    empty.className = 'children-empty';
                    empty.textContent = 'empty';
                    ul.appendChild(empty);
                }}
                li.appendChild(ul);
            }} else {{
                li.classList.add('file');
                nodeSpan.textContent = data.name;
                li.appendChild(nodeSpan);
            }}

            return li;
        }}

        const rootLi = createNode(treeData);
        const rootUl = document.createElement('ul');
        rootUl.appendChild(rootLi);
        document.getElementById('treeContainer').appendChild(rootUl);

        // Flatten for search
        const allNodes = [];
        function flatten(node, element) {{
            allNodes.push({{
                data: node,
                element: element,
                path: node.path,
                name: node.name,
                type: node.type,
            }});
            if (node.type === 'dir' && node.children) {{
                const childUl = element.querySelector('ul');
                if (childUl) {{
                    const childLis = childUl.querySelectorAll(':scope > li');
                    for (let i = 0; i < node.children.length; i++) {{
                        if (i < childLis.length) {{
                            flatten(node.children[i], childLis[i]);
                        }}
                    }}
                }}
            }}
        }}
        flatten(treeData, rootLi);

        // Stats
        const totalDirs = allNodes.filter(n => n.type === 'dir').length;
        const totalFiles = allNodes.filter(n => n.type === 'file').length;
        document.getElementById('stats').textContent = `${{totalDirs}} dirs, ${{totalFiles}} files`;

        // Search
        const searchInput = document.getElementById('searchInput');
        const resultsDiv = document.getElementById('results');

        function getAncestors(element) {{
            const ancestors = [];
            let el = element.parentElement;
            while (el) {{
                if (el.tagName === 'LI' && el.classList.contains('dir')) {{
                    ancestors.push(el);
                }}
                el = el.parentElement;
            }}
            return ancestors;
        }}

        function performSearch() {{
            const pattern = searchInput.value.trim();
            allNodes.forEach(n => {{
                n.element.classList.remove('highlight');
                const pathSpan = n.element.querySelector('.match-path');
                if (pathSpan) pathSpan.remove();
            }});
            resultsDiv.classList.remove('show');
            resultsDiv.innerHTML = '';

            if (pattern === '') return;

            let regex;
            try {{
                regex = new RegExp(pattern);
            }} catch (e) {{
                resultsDiv.textContent = 'Invalid regex: ' + e.message;
                resultsDiv.classList.add('show');
                return;
            }}

            const matches = allNodes.filter(n => regex.test(n.name));

            if (matches.length === 0) {{
                resultsDiv.textContent = 'No matches found.';
                resultsDiv.classList.add('show');
                return;
            }}

            let resultHtml = '<strong>Matches:</strong><br>';
            matches.forEach(n => {{
                resultHtml += `<div class="match-item">${{n.path || '/'}}</div>`;
                n.element.classList.add('highlight');
                const ancestors = getAncestors(n.element);
                ancestors.forEach(anc => {{
                    anc.classList.remove('collapsed');
                    const toggle = anc.querySelector('.toggle');
                    if (toggle) toggle.textContent = '▾';
                }});
                const pathSpan = document.createElement('span');
                pathSpan.className = 'match-path';
                pathSpan.textContent = n.path || '/';
                n.element.appendChild(pathSpan);
            }});
            resultsDiv.innerHTML = resultHtml;
            resultsDiv.classList.add('show');
        }}

        searchInput.addEventListener('input', performSearch);

        document.getElementById('expandAllBtn').addEventListener('click', function() {{
            document.querySelectorAll('.dir.collapsed').forEach(el => {{
                el.classList.remove('collapsed');
                const toggle = el.querySelector('.toggle');
                if (toggle) toggle.textContent = '▾';
            }});
        }});

        document.getElementById('collapseAllBtn').addEventListener('click', function() {{
            document.querySelectorAll('.dir:not(.collapsed)').forEach(el => {{
                el.classList.add('collapsed');
                const toggle = el.querySelector('.toggle');
                if (toggle) toggle.textContent = '▸';
            }});
        }});

        // Collapse all subdirectories initially (root stays expanded)
        document.querySelectorAll('.dir').forEach(el => {{
            if (el !== rootLi) {{
                el.classList.add('collapsed');
                const toggle = el.querySelector('.toggle');
                if (toggle) toggle.textContent = '▸';
            }}
        }});
    </script>
</body>
</html>'''
    return html


def print_matches(tree: Dict, pattern: str) -> None:
    try:
        regex = re.compile(pattern)
    except re.error as e:
        print(f"Invalid regex: {e}", file=sys.stderr)
        sys.exit(1)

    def walk(node: Dict, path: str):
        if regex.search(node["name"]):
            print(node["path"] if node["path"] else "/")
        if node["type"] == "dir":
            for child in node.get("children", []):
                walk(child, path + "/" + child["name"])

    walk(tree, "")


def main():
    parser = argparse.ArgumentParser(
        description="Generate an interactive HTML directory tree with regex search."
    )
    parser.add_argument("path", nargs="?", default=".", help="directory to scan (default: current)")
    parser.add_argument("-a", "--all", action="store_true", help="include hidden files/folders")
    parser.add_argument("-d", "--dirs-only", action="store_true", help="list directories only")
    parser.add_argument("-L", "--max-depth", type=int, default=None, metavar="N",
                        help="limit depth to N levels")
    parser.add_argument("-e", "--exclude", action="append", default=[], metavar="PATTERN",
                        help="extra glob pattern to exclude (repeatable)")
    parser.add_argument("--no-default-excludes", action="store_true",
                        help="don't apply built-in exclusions")
    parser.add_argument("-o", "--output", metavar="FILE", default="tree.html",
                        help="output HTML file (default: tree.html)")
    parser.add_argument("--no-open", action="store_true",
                        help="don't open the HTML in a browser")
    parser.add_argument("--search", metavar="REGEX", help="print matching paths and exit (no HTML generated)")
    args = parser.parse_args()

    root_path = Path(args.path).expanduser().resolve()
    if not root_path.is_dir():
        print(f"Error: not a directory: {root_path}", file=sys.stderr)
        sys.exit(1)

    patterns = set(args.exclude)
    if not args.no_default_excludes:
        patterns |= DEFAULT_EXCLUDES

    tree = walk_directory(root_path, patterns, args.all, args.dirs_only, args.max_depth)

    if args.search:
        print_matches(tree, args.search)
        return

    html_content = generate_html(tree, root_path)
    output_file = Path(args.output)
    output_file.write_text(html_content, encoding="utf-8")
    print(f"🌳 Tree saved to {output_file.resolve()}")

    if not args.no_open:
        webbrowser.open(str(output_file.resolve()))


if __name__ == "__main__":
    main()
