package br.edu.ifsp.ads.pdm.adapter

interface OnTaskClickListener {
  fun onTileTaskClick(position: Int)

  fun onDetailMenuItemClick(position: Int)
  fun onEditMenuItemClick(position: Int)
  fun onDeleteMenuItemClick(position: Int)
  fun onCompletedMenuItemClick(position: Int)
}